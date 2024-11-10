package school.sptech.crudrisecanvas.dtos.mapping;

import lombok.Data;
import school.sptech.crudrisecanvas.dtos.address.AddressResponseDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseNoMappingRelationDto;
import school.sptech.crudrisecanvas.dtos.tags.TagsResponseDto;

import java.util.List;

@Data
public class MappingResponseNoRelationDto {
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
    private AddressResponseDto address;
    private List<TagsResponseDto> tags;
    private List<MappingActionResponseNoMappingRelationDto> mappingActions;
}
