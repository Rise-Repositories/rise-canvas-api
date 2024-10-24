package school.sptech.crudrisecanvas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import school.sptech.crudrisecanvas.entities.Action;


public interface ActionRepository extends JpaRepository<Action, Integer>{
    List<Action> findAllByOngId(Integer ongId);

    @Query(value = """
        select a.* from action a
            join ong o on a.ong_id = o.id
            where :radius >
            6371 * ACOS(
                COS(RADIANS(a.latitude)) * COS(RADIANS(:latitude)) *
                COS(RADIANS(:longitude) - RADIANS(a.longitude)) +
                SIN(RADIANS(a.latitude)) * SIN(RADIANS(:latitude))
            )
    """, nativeQuery = true)
    List<Action> findByCordinates(Double latitude, Double longitude, Double radius);
}