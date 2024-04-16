package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.Ong;

public interface OngRepository extends JpaRepository<Ong, Integer>{
    int countWithEmailOrCnpj(String email, String cnpj);
    int countWithId(int id);
}
