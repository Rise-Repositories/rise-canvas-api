package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.Ong;

public interface OngRepository extends JpaRepository<Ong, Integer>{
    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdNot(String cnpj, Integer id);
}
