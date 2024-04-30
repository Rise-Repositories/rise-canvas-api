package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import school.sptech.crudrisecanvas.entities.User;

import java.util.Optional;

public interface UserRepositary extends JpaRepository<User, Integer>{
    @Query("SELECT count(*) FROM User u WHERE u.email = ?1 OR u.cpf = ?2")
    Integer countWithEmailOrCpf(String email, String cpf);

    @Query("SELECT count(*) FROM User u WHERE u.id <> ?3 and (u.email = ?1 OR u.cpf = ?2)")
    Integer countWithEmailOrCpfAndDiferentId(String email, String cpf, Integer id);
     
    @Query("SELECT count(*) FROM User u WHERE u.id = ?1")
    Integer countWithId(Integer id);

    Optional<User> findByEmail(String email);

    boolean existsByCpf(String cpf);
}
