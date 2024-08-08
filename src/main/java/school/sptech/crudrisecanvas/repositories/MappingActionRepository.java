package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.MappingAction;

public interface MappingActionRepository extends JpaRepository<MappingAction, Integer>{

    void deleteAllByMappingId(Integer id);
} 