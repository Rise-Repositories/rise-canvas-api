package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.UserMapping;

public interface UserMappingRepository extends JpaRepository<UserMapping, Integer>{
    
}
