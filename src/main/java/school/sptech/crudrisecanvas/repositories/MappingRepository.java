package school.sptech.crudrisecanvas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import school.sptech.crudrisecanvas.entities.Mapping;

public interface MappingRepository extends JpaRepository<Mapping, Integer>{
    @Query(value = """
        select * from mapping m
        where :radius >
        6371 * ACOS(
            COS(RADIANS(m.latitude)) * COS(RADIANS(:latitude)) *
            COS(RADIANS(:longitude) - RADIANS(m.longitude)) +
            SIN(RADIANS(m.latitude)) * SIN(RADIANS(:latitude))
        )
    """, nativeQuery = true)
    List<Mapping> findWhenInsideArea(Double latitude, Double longitude, Double radius);
    
} 