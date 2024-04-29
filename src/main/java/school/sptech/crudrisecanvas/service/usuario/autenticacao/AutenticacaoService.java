package school.sptech.crudrisecanvas.service.usuario.autenticacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.UserRepositary;
import school.sptech.crudrisecanvas.service.usuario.autenticacao.dto.UsuarioDetalhesDto;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UserRepositary usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> usuarioOpt = usuarioRepository.findByEmail(username);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuario: %s não encontrado", username));

        } else {
            return new UsuarioDetalhesDto(usuarioOpt.get());
        }
    }
}
