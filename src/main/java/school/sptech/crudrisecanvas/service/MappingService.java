package school.sptech.crudrisecanvas.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.utils.HeatmapGenerator;
import school.sptech.crudrisecanvas.dtos.mapping.MappingAlertDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingGraphDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingHeatmapDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingKpiDto;
import school.sptech.crudrisecanvas.entities.Address;
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
    private final AddressService addressService;

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

    public Mapping createMapping(Mapping mapping, Address address,String token){
        if(mapping.getQtyAdults() + mapping.getQtyChildren() == 0){
            throw new BadRequestException("É necessário que haja pelo menos 1 pessoa no local");
        }
        User user = userService.getAccount(token);
        Address savedAddress = addressService.saveByCep(address.getCep(), address.getNumber(), address.getComplement());

        UserMapping userMapping = userMappingService.createRelation(user, mapping);
        
        mapping.setUsersMappings(List.of(userMapping));
        mapping.setStatus(MappingStatus.ACTIVE);
        mapping.setAddress(savedAddress);

        Mapping savedMapping = mappingRepository.save(mapping);

        return savedMapping;
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

    public List<MappingAlertDto> getMappingAlerts() {
        return mappingRepository.getMappingAlerts();
    }

    public Double[][] getHeatmapPoints(double radiusToGroup, LocalDateTime olderThan) {
        List<MappingHeatmapDto> mappings = mappingRepository.getMappingsHeatmap();

        return HeatmapGenerator.getHeatmapPointsNotHelped(mappings, radiusToGroup, olderThan);
    }

    public MappingKpiDto getKpisAfterDate(LocalDate data) {
        return mappingRepository.getKpisAfterDate(data);
    }

    public MappingKpiDto getKpisTotal() {
        return mappingRepository.getKpisTotal();
    }
    
    public List<MappingGraphDto> getMappingGraph(LocalDate date) {
        return mappingRepository.getChartData(date);
    }
    
}
