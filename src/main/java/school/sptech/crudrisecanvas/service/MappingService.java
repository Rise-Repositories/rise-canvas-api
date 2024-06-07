package school.sptech.crudrisecanvas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.UserMapping;
import school.sptech.crudrisecanvas.exception.BadRequestException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.MappingRepository;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

@Service
@RequiredArgsConstructor
public class MappingService {
    private final UserMappingService userMappingService;
    private final MappingRepository mappingRepository;
    private final UserService userService;

    public List<Mapping> getMappings(){
        return mappingRepository.findAll();
    }

    public Mapping getMappingById(Integer id){
        Optional<Mapping> mapping = mappingRepository.findById(id);
        if(mapping.isEmpty()){
            throw new NotFoundException("Mapeamento não encontrado");
        }

        return mapping.get();
    }

    public Mapping createMapping(Mapping mapping, String token){
        if(mapping.getQtyAdults() + mapping.getQtyChildren() == 0){
            throw new BadRequestException("É necessário que haja pelo menos 1 pessoa no local");
        }
        User user = userService.getAccount(token);

        UserMapping userMapping = userMappingService.createRelation(user, mapping);
        
        mapping.setUsersMappings(List.of(userMapping));
        mapping.setStatus(MappingStatus.ACTIVE);

        return mappingRepository.save(mapping);
    }

    public Mapping updateMapping(Integer id, Mapping mapping){
        Mapping mappingToUpdate = this.getMappingById(id);


        mappingToUpdate.setQtyAdults(mapping.getQtyAdults());
        mappingToUpdate.setQtyChildren(mapping.getQtyChildren());
        mappingToUpdate.setHasDisorders(mapping.getHasDisorders());
        mappingToUpdate.setReferencePoint(mapping.getReferencePoint());
        mappingToUpdate.setDescription(mapping.getDescription());
        mappingToUpdate.setLatitude(mapping.getLatitude());
        mappingToUpdate.setLongitude(mapping.getLongitude());

        return mappingRepository.save(mappingToUpdate);
    }

    public void deleteMapping(Integer id){
        Mapping mapping = this.getMappingById(id);
        mappingRepository.delete(mapping);
    }

    public Mapping addUser(Integer id, Integer userId){
        Mapping mapping = this.getMappingById(id);
        User user = userService.getUserById(userId);

        Mapping response = mappingRepository.save(mapping);

        userMappingService.createRelation(user, response);

        return response;
    }
}
