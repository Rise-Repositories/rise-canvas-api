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
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.ConflictException;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.UserRepositary;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositary userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager gerenciadorTokenJwt;
    private final AuthenticationManager authenticationManager;

    public void register(User newUser) {

        String passwordHash = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(passwordHash);

        this.userRepository.save(newUser);
    }

    public UserTokenDto autenticar(UserLoginDto UserLoginDto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                UserLoginDto.getEmail(), UserLoginDto.getPassword()
        );
        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        User UserAutenticado = userRepository.findByEmail(UserLoginDto.getEmail())
                .orElseThrow(() -> new NotFoundException("Email do usuário não cadastrado"));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = gerenciadorTokenJwt.generateToken(authentication);
        return UserMapper.toTokenDto(UserAutenticado, token);
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

    public User updateUser(int id, User user, String token) {
        User userToUpdate = this.getUserById(id);
        User userLogged = this.getAccount(token);

        if(userLogged.getId() != userToUpdate.getId()){
            throw new ForbiddenException("Você não tem permissão para fazer esta ação");
        }

        if(
            this.userRepository.existsByCpfAndIdNot(userToUpdate.getCpf(), id)
        ){
            throw new ConflictException("Já existe um usuário com este CPF");
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
}