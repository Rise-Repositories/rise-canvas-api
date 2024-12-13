package school.sptech.crudrisecanvas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import school.sptech.crudrisecanvas.dtos.mapping.MappingAlertDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingGraphDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingHeatmapDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingKpiDto;
import school.sptech.crudrisecanvas.entities.Mapping;

import java.time.LocalDate;

public interface MappingRepository extends JpaRepository<Mapping, Integer>{

    @Query(value = """
            SELECT m.id AS MappingId, m.date AS Date, CONCAT(adr.street, ', ', adr.number, ' - ', adr.neighbourhood) AS Address, a.datetime_start AS LastServed
                        	FROM mapping m INNER JOIN address adr ON adr.id = m.address_id
                            LEFT JOIN mapping_action ma on m.id = ma.mapping_id
                            LEFT JOIN action a ON ma.action_id = a.id
                            WHERE ma.id IS NULL
                            UNION
            SELECT m.id AS MappingId, m.date AS Date, CONCAT(adr.street, ', ', adr.number, ' - ', adr.neighbourhood) AS Address, MAX(a.datetime_start) AS LastServed
                        	FROM mapping m INNER JOIN address adr ON adr.id = m.address_id
                            LEFT JOIN mapping_action ma on m.id = ma.mapping_id
                            LEFT JOIN action a ON ma.action_id = a.id
                            WHERE a.datetime_start = (
                        		SELECT MAX(a.datetime_start) FROM mapping m2
                                LEFT JOIN mapping_action ma on m2.id = ma.mapping_id
                        		LEFT JOIN action a ON ma.action_id = a.id AND m2.id = m.id AND a.datetime_start < ?1)
                            GROUP BY m.id;""", nativeQuery = true)
    List<MappingAlertDto> getMappingAlerts(LocalDate beforeDate);

    @Query(value = """
            SELECT m.id AS MappingId, m.latitude AS Latitude, m.longitude AS Longitude, m.qty_adults AS QtyAdults, m.qty_children AS QtyChildren, ma.no_donation AS NoDonation, a.datetime_start AS DatetimeStart
            	FROM mapping m LEFT JOIN mapping_action ma on m.id = ma.mapping_id
                LEFT JOIN action a ON ma.action_id = a.id
                WHERE ma.id IS NULL
                AND EXISTS(
                    SELECT * FROM mapping_tags mt WHERE m.id = mt.mapping_id AND mt.tags_id IN ?1
                )
                UNION
            SELECT m.id AS MappingId, m.latitude AS Latitude, m.longitude AS Longitude, m.qty_adults AS QtyAdults, m.qty_children AS QtyChildren, MAX(ma.no_donation) AS NoDonation, MAX(a.datetime_start) AS DatetimeStart
            	FROM mapping m LEFT JOIN mapping_action ma on m.id = ma.mapping_id
                LEFT JOIN action a ON ma.action_id = a.id
                WHERE NOT ma.no_donation AND a.datetime_start = (
            		SELECT MAX(a.datetime_start) FROM mapping m2
                    LEFT JOIN mapping_action ma on m2.id = ma.mapping_id
            		LEFT JOIN action a ON ma.action_id = a.id AND m2.id = m.id)
                AND EXISTS(
                    SELECT * FROM mapping_tags mt WHERE m.id = mt.mapping_id AND mt.tags_id IN ?1
                )
                GROUP BY m.id;""", nativeQuery = true)
    List<MappingHeatmapDto> getMappingsHeatmap(List<Integer> tagIds);

    @Query(value = """
            SELECT
                COUNT(m.id) AS QtyTotal,
                SUM(CASE WHEN ma.no_people = 0 AND a.datetime_start > ?1 AND a.datetime_start < ?2 THEN 1 ELSE 0 END) AS QtyServed,
                SUM(CASE WHEN ma.id IS NULL OR a.datetime_start < ?1 OR a.datetime_start > ?2 THEN 1 ELSE 0 END) AS QtyNotServed,
                SUM(CASE WHEN ma.no_people = 1 AND a.datetime_start > ?1 AND a.datetime_start < ?2 THEN 1 ELSE 0 END) AS QtyNoPeople
                FROM mapping m LEFT JOIN mapping_action ma on m.id = ma.mapping_id
                LEFT JOIN action a ON ma.action_id = a.id
                WHERE ma.id IS NULL OR
                    a.datetime_start = (
                        SELECT MAX(a.datetime_start) FROM mapping m2
                        LEFT JOIN mapping_action ma on m2.id = ma.mapping_id
                        LEFT JOIN action a ON ma.action_id = a.id AND m2.id = m.id
                        WHERE a.datetime_start < ?2 AND a.datetime_end > ?1)
                    AND EXISTS(
                        SELECT * FROM mapping_tags mt WHERE m.id = mt.mapping_id AND mt.tags_id IN ?3
                    ) LIMIT 1;""", nativeQuery = true)
    MappingKpiDto getKpisByDates(LocalDate startDate, LocalDate endDate, List<Integer> tagIds);

    @Query(value = "call graph(:startDate, :endDate, :tagIds)", nativeQuery = true )
    List<MappingGraphDto> getChartData(LocalDate startDate, LocalDate endDate, String tagIds);

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

    @Query(value = """
        select m.* from mapping m INNER JOIN mapping_action ma
        ON m.id = ma.mapping_id
        where ma.action_id = :actionId AND 
        :radius >
        6371 * ACOS(
            COS(RADIANS(m.latitude)) * COS(RADIANS(:latitude)) *
            COS(RADIANS(:longitude) - RADIANS(m.longitude)) +
            SIN(RADIANS(m.latitude)) * SIN(RADIANS(:latitude))
        )
    """, nativeQuery = true)
    List<Mapping> findWhenInsideAreaDonated(Double latitude, Double longitude, Double radius, Integer actionId);

    @Query(value = """
        SELECT * FROM mapping m
        WHERE m.date BETWEEN ?1 AND ?2
        """, nativeQuery = true)
    List<Mapping> getMappingsByDate(LocalDate startDate, LocalDate endDate);
    
    @Query(value = """
        select m.* from mapping m
        join user_mapping um on m.id = um.mapping_id
        join user u on um.user_id = u.id
        where :radius >
        6371 * ACOS(
            COS(RADIANS(m.latitude)) * COS(RADIANS(:latitude)) *
            COS(RADIANS(:longitude) - RADIANS(m.longitude)) +
            SIN(RADIANS(m.latitude)) * SIN(RADIANS(:latitude))
        ) AND u.id = :id
    """, nativeQuery = true)
    List<Mapping> findWhenInsideAreaByUser(Double latitude, Double longitude, Double radius, Integer id);

}
