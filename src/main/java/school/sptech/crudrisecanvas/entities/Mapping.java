package school.sptech.crudrisecanvas.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;
import school.sptech.crudrisecanvas.utils.Converters.MappingStatusConvert;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

@Entity
@Data
public class Mapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer qtyAdults;
    private Integer qtyChildren;
    private String referencePoint;
    private Boolean hasDisorders;
    private String description;
    private Double latitude;
    private Double longitude;

    @Convert(converter = MappingStatusConvert.class)
    private MappingStatus status;

    @CreationTimestamp
    private LocalDate date;

    @OneToMany(mappedBy = "mapping")
    private List<UserMapping> usersMappings;

    @OneToMany(mappedBy = "mapping")
    private List<MappingAction> mappingActions;

    @ManyToOne
    private Address address;

    @ManyToMany
    @JoinTable(name = "mapping_tags",uniqueConstraints = @UniqueConstraint(columnNames = {"mapping_id", "tags_id"}))
    private List<Tags> tags;
}