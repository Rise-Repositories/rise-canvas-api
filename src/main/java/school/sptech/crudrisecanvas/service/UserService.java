package school.sptech.crudrisecanvas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.crudrisecanvas.api.configuration.security.jwt.JwtTokenManager;
import school.sptech.crudrisecanvas.dtos.user.UserLoginDto;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;
import school.sptech.crudrisecanvas.dtos.user.UserRequestMapper;
import school.sptech.crudrisecanvas.dtos.user.UserResponseMapper;
import school.sptech.crudrisecanvas.dtos.user.UserTokenDto;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.UserRepositary;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepositary UserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenManager gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void criar(UserRequestDto novoUserDto) {
        final User novoUser = UserRequestMapper.toEntity(novoUserDto);

        String senhaCriptografada = passwordEncoder.encode(novoUser.getPassword());
        novoUser.setPassword(senhaCriptografada);

        this.UserRepository.save(novoUser);
    }

    public UserTokenDto autenticar(UserLoginDto UserLoginDto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                UserLoginDto.getEmail(), UserLoginDto.getPassword()
        );
        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        User UserAutenticado = UserRepository.findByEmail(UserLoginDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(404, "Email do usuário não cadastrado", null));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = gerenciadorTokenJwt.generateToken(authentication);
        return UserResponseMapper.toTokenDto(UserAutenticado, token);
    }

    public List<User> listar() {
        return this.UserRepository.findAll();
    }

    public boolean existePorCpf(String cpf) {
        return this.UserRepository.existsByCpf(cpf);
    }
}