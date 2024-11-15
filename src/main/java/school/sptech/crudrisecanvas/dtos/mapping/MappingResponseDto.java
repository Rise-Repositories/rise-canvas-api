package school.sptech.crudrisecanvas.dtos.mapping;

import java.util.List;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.address.AddressResponseDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseNoMappingRelationDto;
import school.sptech.crudrisecanvas.dtos.tags.TagsResponseDto;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingMappingResponsDto;

@Data
public class MappingResponseDto {
    private Integer id;
    private Integer qtyAdults;
    private Integer qtyChildren;
    private Boolean hasDisorders;
    private String referencePoint;
    private String description;
    private Double latitude;
    private Double longitude;
    private String status;
    private String date;
    private List<UserMappingMappingResponsDto> userMappings;
    private List<MappingActionResponseNoMappingRelationDto> mappingActions;
    private AddressResponseDto address;
    private List<TagsResponseDto> tags;
}
