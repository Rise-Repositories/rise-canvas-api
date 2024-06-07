package school.sptech.crudrisecanvas.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountDto;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.UserMapping;
import school.sptech.crudrisecanvas.repositories.UserMappingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMappingService {
    private final UserMappingRepository userMappingRepository;

    public UserMapping createRelation(User user, Mapping mapping){
        UserMapping userMapping = new UserMapping();
        userMapping.setUser(user);
        userMapping.setMapping(mapping);

        return userMappingRepository.save(userMapping);
    }

    public List<UserMappingCountDto> getMappingCountByUser() {
        return userMappingRepository.getMappingCountByUser();
    }
}
