package school.sptech.crudrisecanvas.dtos.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import school.sptech.crudrisecanvas.entities.User;

import java.util.Collection;

public class UserDetailsDto implements UserDetails {

    private final String name;
    @Getter
    private final String email;
    private final String password;
    @Getter
    private final String cpf;
    @Getter
    private final String ip;


    public UserDetailsDto(User usuario) {
        this.name = usuario.getName();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
        this.cpf = usuario.getCpf();
        this.ip = usuario.getIp();
    }

    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
