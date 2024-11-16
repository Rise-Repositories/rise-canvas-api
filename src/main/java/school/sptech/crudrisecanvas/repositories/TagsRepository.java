package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import school.sptech.crudrisecanvas.entities.Tags;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags, Integer> {

    @Query("select t.id from Tags t")
    public List<Integer> getAllIds();
}
