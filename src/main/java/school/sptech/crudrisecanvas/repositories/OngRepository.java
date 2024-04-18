package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import school.sptech.crudrisecanvas.entities.Ong;

public interface OngRepository extends JpaRepository<Ong, Integer>{
    @Query("SELECT COUNT(o) FROM Ong o WHERE o.email = ?1 OR o.cnpj = ?2")
    int countWithEmailOrCnpj(String email, String cnpj);
}
