package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountDto;
import school.sptech.crudrisecanvas.entities.UserMapping;

import java.util.List;

public interface UserMappingRepository extends JpaRepository<UserMapping, Integer>{

    @Query("""
            SELECT new school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountDto(COUNT(um.id), um.user.id)
            FROM UserMapping AS um GROUP BY um.user.id""")
    List<UserMappingCountDto> getMappingCountByUser();
}
