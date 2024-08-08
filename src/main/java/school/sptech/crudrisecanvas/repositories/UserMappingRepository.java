package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountDto;
import school.sptech.crudrisecanvas.entities.UserMapping;

import java.util.List;

public interface UserMappingRepository extends JpaRepository<UserMapping, Integer>{

    @Query("""
            SELECT new school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountDto(COUNT(um.id), u.id)
            FROM User u LEFT JOIN UserMapping um ON u.id = um.user.id GROUP BY u.id""")
    List<UserMappingCountDto> getMappingCountByUser();
}
