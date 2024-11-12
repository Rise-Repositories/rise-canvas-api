package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.Tags;

public interface TagsRepository extends JpaRepository<Tags, Integer> {
    
}
