package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.Action;


public interface ActionRepository extends JpaRepository<Action, Integer>{

    
}