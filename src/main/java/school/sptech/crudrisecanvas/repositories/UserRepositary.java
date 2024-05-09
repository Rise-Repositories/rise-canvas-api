package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.User;

import java.util.Optional;

public interface UserRepositary extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpfAndNotEqualId(String cpf, Integer id);

    boolean existsByEmailAndNotEqualId(String email, Integer id);
}
