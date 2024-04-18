package school.sptech.crudrisecanvas.entities;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import school.sptech.crudrisecanvas.Utils.Converters.MappingStatusConvert;
import school.sptech.crudrisecanvas.Utils.Enums.MappingStatus;

@Entity
@Data
public class Mapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer qtyPeople;
    private String description;
    private Double latitute;
    private Double longitude;

    @Convert(converter = MappingStatusConvert.class)
    private MappingStatus status;

    @CreationTimestamp
    private LocalDate date;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "mapping")
    private List<MappingAction> mappingActions;
}