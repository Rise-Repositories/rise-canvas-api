package school.sptech.crudrisecanvas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.MappingActionRepository;
import school.sptech.crudrisecanvas.repositories.MappingRepository;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

@Service
@RequiredArgsConstructor
public class MappingService {
    private final MappingActionRepository mappingActionRepository;
    private final MappingRepository mappingRepository;
    private final UserService userService;

    public List<Mapping> getMappings(){
        return mappingRepository.findAll();
    }

    public Mapping getMappingById(Integer id){
        Optional<Mapping> mapping = mappingRepository.findById(id);
        if(mapping.isEmpty()){
            throw new NotFoundException("Mappeamento não encontrado");
        }

        return mapping.get();
    }

    public Mapping createMapping(Mapping mapping){
        //TODO: tem que pegar o token e buscar o usuário
        User user = userService.getUserById(1);
        
        mapping.setUsers(List.of(user));
        mapping.setStatus(MappingStatus.ACTIVE);

        return mapping;
    }

    public Mapping updateMapping(Integer id, Mapping mapping){
        Mapping mappingToUpdate = this.getMappingById(id);

        mappingToUpdate.setQtyPeople(mapping.getQtyPeople());
        mappingToUpdate.setDescription(mapping.getDescription());
        mappingToUpdate.setLatitude(mapping.getLatitude());
        mappingToUpdate.setLongitude(mapping.getLongitude());

        return mappingRepository.save(mappingToUpdate);
    }

    public void deleteMapping(Integer id){
        Mapping mapping = this.getMappingById(id);

        List<Integer> ids = mapping.getMappingActions().stream().map(e -> e.getId()).toList();
        mappingActionRepository.deleteAllById(ids);
        mappingRepository.delete(mapping);
    }

    public Mapping addUser(Integer id, Integer userId){
        Mapping mapping = this.getMappingById(id);
        User user = userService.getUserById(userId);

        List<User> users = mapping.getUsers() == null 
            ? new ArrayList<>() 
            : mapping.getUsers();

        users.add(user);

        mapping.setUsers(users);

        return mappingRepository.save(mapping);
    }
}