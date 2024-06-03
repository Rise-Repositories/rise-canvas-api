package school.sptech.crudrisecanvas.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.UserMapping;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.UserMappingRepository;

@Service
@RequiredArgsConstructor
public class UserMappingService {
    private final UserMappingRepository repository;

    public UserMapping createRelation(User user, Mapping mapping){
        UserMapping userMapping = new UserMapping();

        userMapping.setUser(user);
        userMapping.setMapping(mapping);

        return repository.save(userMapping);
    }

    public void deleteRelation(int id){
        if(repository.existsById(id)){
            repository.deleteById(id);
        }else{
            throw new NotFoundException("ID não encontrado");
        }
    }
}
