package school.sptech.crudrisecanvas.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.api.configuration.security.jwt.JwtTokenManager;
import school.sptech.crudrisecanvas.dtos.user.UserLoginDto;
import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.dtos.user.UserTokenDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.BadRequestException;
import school.sptech.crudrisecanvas.exception.ConflictException;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.UserRepositary;
import school.sptech.crudrisecanvas.utils.adpters.MailValue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                UserAutenticado.getId(), UserLoginDto.getPassword()
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

    public User getUserById(Integer id) {
        Optional<User> user = this.userRepository.findById(id);

        if(user.isEmpty()){
            throw new NotFoundException("Usuário não encontrado");
        }

        return user.get();
    }

    public User getAccount(String token) {
        String username = gerenciadorTokenJwt.getUsernameFromToken(token);

        return userRepository.findByEmail(username)
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

    public void deleteUser(int id) {
        User user = this.getUserById(id);
        this.userRepository.delete(user);
    }

    public Long getUserCount() {
        return this.userRepository.count();
    }
}