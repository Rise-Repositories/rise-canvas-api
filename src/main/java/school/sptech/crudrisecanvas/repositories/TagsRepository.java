package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import school.sptech.crudrisecanvas.entities.Tags;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tags, Integer> {

    @Query("select t.id from Tags t")
    List<Integer> getAllIds();

    @Query(value = "select t.id AS Id, t.name AS Name from tags t JOIN mapping_tags mt ON t.id = mt.tags_id WHERE mt.mapping_id = ?1", nativeQuery = true)
    List<Tags> getAllTagsByMappingId(Integer mappingId);
}
