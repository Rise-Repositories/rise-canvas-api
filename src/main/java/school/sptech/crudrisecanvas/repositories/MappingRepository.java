package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import school.sptech.crudrisecanvas.dtos.mapping.MappingHeatmapDto;
import school.sptech.crudrisecanvas.entities.Mapping;

import java.util.List;

public interface MappingRepository extends JpaRepository<Mapping, Integer>{

    @Query(value = """
            SELECT m.id AS MappingId, m.latitude AS Latitude, m.longitude AS Longitude, m.qty_adults AS QtyAdults, m.qty_children AS QtyChildren, ma.no_donation AS NoDonation, a.datetime_start AS DatetimeStart
            	FROM mapping m LEFT JOIN Mapping_Action ma on m.id = ma.mapping_id
                LEFT JOIN Action a ON ma.action_id = a.id
                WHERE ma.id IS NULL
                UNION
            SELECT m.id AS MappingId, m.latitude AS Latitude, m.longitude AS Longitude, m.qty_adults AS QtyAdults, m.qty_children AS QtyChildren, MAX(ma.no_donation) AS NoDonation, MAX(a.datetime_start) AS DatetimeStart
            	FROM mapping m LEFT JOIN Mapping_Action ma on m.id = ma.mapping_id
                LEFT JOIN Action a ON ma.action_id = a.id
                WHERE NOT ma.no_donation AND a.datetime_start = (
            		SELECT MAX(a.datetime_start) FROM mapping m2
                    LEFT JOIN Mapping_Action ma on m2.id = ma.mapping_id
            		LEFT JOIN Action a ON ma.action_id = a.id AND m2.id = m.id)
                GROUP BY m.id;""", nativeQuery = true)
    List<MappingHeatmapDto> getMappingsHeatmap();
} 