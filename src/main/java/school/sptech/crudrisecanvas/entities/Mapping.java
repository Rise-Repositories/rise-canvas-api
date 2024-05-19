package school.sptech.crudrisecanvas.entities;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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

    @ManyToMany
    private List<User> users;

    @OneToMany(mappedBy = "mapping")
    private List<MappingAction> mappingActions;
}