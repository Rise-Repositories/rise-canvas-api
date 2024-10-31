package school.sptech.crudrisecanvas.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.crudrisecanvas.api.configuration.security.jwt.JwtTokenManager;
import school.sptech.crudrisecanvas.dtos.user.*;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.BadRequestException;
import school.sptech.crudrisecanvas.exception.ConflictException;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.UserRepositary;
import school.sptech.crudrisecanvas.utils.ResponseLambda;
import school.sptech.crudrisecanvas.utils.adpters.MailValue;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.LambdaException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositary userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;
    private final AddressService addressService;

    public User register(User newUser) {
        if(userRepository.existsByCpf(newUser.getCpf())){
            throw new ConflictException("CPF já cadastrado");
        }
        if(userRepository.existsByEmail(newUser.getEmail())){
            throw new ConflictException("E-mail já cadastrado");
        }

        if (newUser.getAddress() != null) {
            Address savedAddress = addressService.saveByCep(newUser.getAddress().getCep(),
                    newUser.getAddress().getNumber(),
                    newUser.getAddress().getComplement());
            newUser.setAddress(savedAddress);
        } else {
            newUser.setAddress(null);
        }

        String passwordHash = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(passwordHash);

        return this.userRepository.save(newUser);
    }

    public UserTokenDto autenticar(UserLoginDto UserLoginDto) {
        User UserAutenticado = userRepository.findByEmail(UserLoginDto.getEmail())
                .orElseThrow(() -> new NotFoundException("Email do usuário não cadastrado"));

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                UserAutenticado.getId().toString(), UserLoginDto.getPassword()
        );
        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = gerenciadorTokenJwt.generateToken(authentication);
        return UserMapper.toTokenDto(UserAutenticado, token);
    }

    public void recover(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Email do usuário não cadastrado"));

        LocalDateTime limiteTime = LocalDateTime.now().plusMinutes(10);

        user.setAllowedToUpdate(limiteTime);

        userRepository.save(user);

        ScheduleService.add(
            new MailValue(
                email,
                "Recuperação de senha",
                String.format("""
                    <h1>Olá, deseja alterar sua senha?</h1>
                    <a href="http://localhost:3000/change-password/%s">Alterar</a>
                """, user.getId())
            )
        );
    }

    public void changePassword(Integer id, String password) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if(user.getAllowedToUpdate() == null){
            throw new ForbiddenException("Ainda não solicitou alteração de senha");
        }

        if(user.getAllowedToUpdate().isBefore(LocalDateTime.now())){
            throw new ForbiddenException("Tempo para alteração de senha expirado");
        }

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void patchPassword(UserRequestPatchPasswordDto request, String token) {
        User user = getAccount(token);


        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                user.getId().toString(), request.getCurPassword()
        );
        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public User getUserById(Integer id) {
        Optional<User> user = this.userRepository.findById(id);

        if(user.isEmpty()){
            throw new NotFoundException("Usuário não encontrado");
        }

        return user.get();
    }

    public User getAccount(String token) {
        String username = gerenciadorTokenJwt.getUsernameFromToken(token);

        return userRepository.findById(Integer.parseInt(username))
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public List<User> getUsersByEmail(String email) {
        if(email.isBlank()) throw new BadRequestException("Email nao informado");

        return userRepository.findAllByEmailContainingIgnoreCase(email);
    }

    public User updateUser(int id, User user, String token) {
        User userToUpdate = this.getUserById(id);
        User userLogged = this.getAccount(token);

        if(userLogged.getId() != userToUpdate.getId()){
            throw new ForbiddenException("Você não tem permissão para fazer esta ação");
        }

        if(
            this.userRepository.existsByCpfAndIdNot(user.getCpf(), id)
        ){
            throw new ConflictException("Já existe um usuário com este CPF");
        }
        if (this.userRepository.existsByEmailAndIdNot(user.getEmail(), id)) {
            throw new ConflictException("Já existe um usuário com este e-mail");
        }

        if (user.getAddress() != null) {
            Address savedAddress = addressService.saveByCep(user.getAddress().getCep(),
                    user.getAddress().getNumber(),
                    user.getAddress().getComplement());
            userToUpdate.setAddress(savedAddress);
        }

        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setCpf(user.getCpf());

        return userRepository.save(userToUpdate);
    }

    public User patchUser(int id, User user, String token) {
        User userToUpdate = this.getUserById(id);
        User userLogged = this.getAccount(token);

        if(userLogged.getId() != userToUpdate.getId()){
            throw new ForbiddenException("Você não tem permissão para fazer esta ação");
        }

        if(this.userRepository.existsByCpfAndIdNot(user.getCpf(), id)){
            throw new ConflictException("Já existe um usuário com este CPF");
        }

        if (this.userRepository.existsByEmailAndIdNot(user.getEmail(), id)) {
            throw new ConflictException("Já existe um usuário com este e-mail");
        }

        if (user.getAddress() != null) {
            Address savedAddress = addressService.saveByCep(user.getAddress().getCep(),
                    user.getAddress().getNumber(),
                    user.getAddress().getComplement());
            userToUpdate.setAddress(savedAddress);
        }
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        if (user.getName() != null && !user.getName().isEmpty() && !user.getName().isBlank()) {
            Set<ConstraintViolation<UserRequestPatchDto>> violations = validator.validateValue(UserRequestPatchDto.class, "name", user.getName());
            if (violations.isEmpty()) {
                userToUpdate.setName(user.getName());
            } else {
                throw new ConstraintViolationException(violations);
            }
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty() && !user.getEmail().isBlank()) {
            Set<ConstraintViolation<UserRequestPatchDto>> violations = validator.validateValue(UserRequestPatchDto.class, "email", user.getEmail());
            if (violations.isEmpty()) {
                userToUpdate.setEmail(user.getEmail());
            } else {
                throw new ConstraintViolationException(violations);
            }
        }
        if (user.getCpf() != null && !user.getCpf().isEmpty() && !user.getCpf().isBlank()) {
            Set<ConstraintViolation<UserRequestPatchDto>> violations = validator.validateValue(UserRequestPatchDto.class, "cpf", user.getCpf());
            if (violations.isEmpty()) {
                userToUpdate.setCpf(user.getCpf());
            } else {
                throw new ConstraintViolationException(violations);
            }
        }

        return userRepository.save(userToUpdate);
    }

    public void deleteUser(int id) {
        User user = this.getUserById(id);
        this.userRepository.delete(user);
    }

    public Long getUserCount() {
        return this.userRepository.count();
    }


    private S3Client criarClienteS3() {
        Region region = Region.US_EAST_1;
        S3Client s3 = S3Client.builder().region(region).build();
        return s3;
    }

    public byte[] getFile(int id, String token) {

        String s3Id = null;

        if (id == -1) {
            s3Id = "layout.pdf";
        } else {
            User user = getAccount(token);

            if (user.getId() != id) {
                throw new ForbiddenException("Você não tem permissão para fazer esta ação");
            }
            s3Id = user.getPhotoId();
        }

        byte[] arquivo = null;

        if (s3Id != null) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket("s3-rise-iacb")
                .key(s3Id)
                .build();

            S3Client s3 = criarClienteS3();
            arquivo = s3.getObjectAsBytes(getObjectRequest).asByteArray();
        }
        return arquivo;
    }

    public void postFile(int id, String token, String fileBase64) {
        User user = getAccount(token);
        if (user.getId() != id) {
            throw new ForbiddenException("Você não tem permissão para fazer esta ação");
        }

        String s3Id = user.getPhotoId();

        if (s3Id != null) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket("s3-rise-iacb")
                    .key(s3Id)
                    .build();

            S3Client s3 = criarClienteS3();
            s3.deleteObject(deleteObjectRequest);
        }

        s3Id = UUID.randomUUID().toString();

        String funcao = "rise-lambda";
        Region region = Region.US_EAST_1;

        LambdaClient awsLambda = LambdaClient.builder()
                .region(region)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        InvokeResponse res = null;

        try {
            Map<String, String> parametros = Map.of("userId", s3Id, "file", fileBase64);

            SdkBytes payload = SdkBytes.fromUtf8String(objectMapper.writeValueAsString(parametros));

            InvokeRequest request = InvokeRequest.builder()
                    .functionName(funcao)
                    .payload(payload)
                    .build();

            res = awsLambda.invoke(request);

            String value = res.payload().asUtf8String();

            ResponseLambda respostaLambda =
                    objectMapper.readValue(value, ResponseLambda.class);

            if (respostaLambda.getStatusCode() != 200) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao salvar a imagem");
            } else {
                String format = fileBase64.split(";")[0].split("/")[1];
                s3Id += '.' + format;
                userRepository.updatePhotoId(user.getId(), s3Id);
            }

        } catch (LambdaException | JsonProcessingException e) {
            System.err.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        awsLambda.close();
    }
}