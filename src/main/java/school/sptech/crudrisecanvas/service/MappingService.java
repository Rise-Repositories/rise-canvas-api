package school.sptech.crudrisecanvas.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.*;
import school.sptech.crudrisecanvas.utils.HeatmapGenerator;
import school.sptech.crudrisecanvas.dtos.mapping.MappingAlertDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingGraphDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingHeatmapDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingKpiDto;
import school.sptech.crudrisecanvas.exception.BadRequestException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.MappingRepository;
import school.sptech.crudrisecanvas.utils.Coordinates;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

@Service
@RequiredArgsConstructor
public class MappingService {
    private final UserMappingService userMappingService;
    private final MappingRepository mappingRepository;
    private final UserService userService;
    private final AddressService addressService;
    private final TagsService tagsService;

    public List<Mapping> getMappings(){
        return mappingRepository.findAll();
    }

    public List<Mapping> getMappingsByDate(LocalDate startDate, LocalDate endDate){
        return mappingRepository.getMappingsByDate(startDate, endDate);
    }

    public List<Mapping> getMappingsByCoordinates(Coordinates coordinates, Double radius){
        return mappingRepository.findWhenInsideArea(
            coordinates.getLatitude(), 
            coordinates.getLongitude(), 
            radius
        );
    }

    public List<Mapping> getDonatedMappingsByCoordinates(Coordinates coordinates, Double radius, Integer actionId){
        return mappingRepository.findWhenInsideAreaDonated(
                coordinates.getLatitude(),
                coordinates.getLongitude(),
                radius,
                actionId
        );
    }

    public List<Mapping> getMappingsByCoordinates(Coordinates coordinates, Double radius, String token){
        Integer id = userService.getAccount(token).getId();

        return mappingRepository.findWhenInsideAreaByUser(
            coordinates.getLatitude(), 
            coordinates.getLongitude(), 
            radius,
            id
        );
    }

    public Mapping getMappingById(Integer id){
        Optional<Mapping> mapping = mappingRepository.findById(id);
        if(mapping.isEmpty()){
            throw new NotFoundException("Mapeamento não encontrado");
        }

        return mapping.get();
    }

    public Mapping createMapping(Mapping mapping, List<Integer> tagsId, String token){
        if(mapping.getQtyAdults() + mapping.getQtyChildren() == 0){
            throw new BadRequestException("É necessário que haja pelo menos 1 pessoa no local");
        }
        User user = userService.getAccount(token);

        if (mapping.getAddress() != null) {
            Address savedAddress = addressService.saveByCep(mapping.getAddress().getCep(),
                    mapping.getAddress().getNumber(),
                    mapping.getAddress().getComplement());
            mapping.setAddress(savedAddress);
        } else {
            mapping.setAddress(null);
        }

        List<Tags> tags = this.tagsService.getManyByIds(tagsId);

        mapping.setStatus(MappingStatus.ACTIVE);
        mapping.setTags(tags);

        Mapping savedMapping = mappingRepository.save(mapping);

        userMappingService.createRelation(user, savedMapping);

        return savedMapping;
    }

    public Mapping createMappingWithoutCepValidation(Mapping mapping, String token){
        if(mapping.getQtyAdults() + mapping.getQtyChildren() == 0){
            throw new BadRequestException("É necessário que haja pelo menos 1 pessoa no local");
        }
        if (mapping.getAddress() != null) {
            Address savedAddress = addressService.save(mapping.getAddress());
            mapping.setAddress(savedAddress);
        } else {
            mapping.setAddress(null);
        }

        mapping.setStatus(MappingStatus.ACTIVE);

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

    public Mapping addUser(Integer id, String token){

        User user = userService.getAccount(token);
        Mapping mapping = this.getMappingById(id);

//        Mapping response = mappingRepository.save(mapping);

        userMappingService.createRelation(user, mapping);

        return mapping;
    }

    public List<MappingAlertDto> getMappingAlerts(LocalDate beforeDate) {
        return mappingRepository.getMappingAlerts(beforeDate);
    }

    public Double[][] getHeatmapPoints(double radiusToGroup, LocalDateTime olderThan) {
        List<MappingHeatmapDto> mappings = mappingRepository.getMappingsHeatmap();

        return HeatmapGenerator.getHeatmapPointsNotHelped(mappings, radiusToGroup, olderThan);
    }

    public MappingKpiDto getKpisByDates(LocalDate startDate, LocalDate endDate) {
        return mappingRepository.getKpisByDates(startDate, endDate);
    }
    
    public List<MappingGraphDto> getMappingGraph(LocalDate startDate, LocalDate endDate) {
        return mappingRepository.getChartData(startDate, endDate);
    }
    
}
