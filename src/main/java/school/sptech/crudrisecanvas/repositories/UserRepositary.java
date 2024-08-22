package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositary extends JpaRepository<User, Integer>{
    Optional<User> findByEmail(String email);

    List<User> findAllByEmailContainingIgnoreCase(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpfAndIdNot(String cpf, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);
}
