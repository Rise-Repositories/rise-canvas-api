package school.sptech.crudrisecanvas.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.crudrisecanvas.api.configuration.security.jwt.GerenciadorTokenJwt;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.UserRepositary;
import school.sptech.crudrisecanvas.service.usuario.autenticacao.dto.UsuarioLoginDto;
import school.sptech.crudrisecanvas.service.usuario.autenticacao.dto.UsuarioTokenDto;
import school.sptech.crudrisecanvas.service.usuario.dto.UsuarioCriacaoDto;
import school.sptech.crudrisecanvas.service.usuario.dto.UsuarioMapper;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UserRepositary usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticationManager;

    public void criar(UsuarioCriacaoDto novoUsuarioDto) {
        final User novoUsuario = UsuarioMapper.toEntity(novoUsuarioDto);

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getPassword());
        novoUsuario.setPassword(senhaCriptografada);

        this.usuarioRepository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.getEmail(), usuarioLoginDto.getPassword()
        );
        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        User usuarioAutenticado = usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(404, "Email do usuário não cadastrado", null));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = gerenciadorTokenJwt.generateToken(authentication);
        return UsuarioMapper.toDto(usuarioAutenticado, token);
    }

    public List<User> listar() {
        return this.usuarioRepository.findAll();
    }

    public boolean existePorCpf(String cpf) {
        return this.usuarioRepository.existsByCpf(cpf);
    }
}
