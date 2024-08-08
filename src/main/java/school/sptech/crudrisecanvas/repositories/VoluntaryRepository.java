package school.sptech.crudrisecanvas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.Voluntary;

public interface VoluntaryRepository extends JpaRepository<Voluntary, Integer>{
    List<Voluntary> findAllByOngId(Integer id);
    
}
