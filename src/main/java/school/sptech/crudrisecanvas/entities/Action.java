package school.sptech.crudrisecanvas.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;

    private LocalDateTime datetimeStart;

    private LocalDateTime datetimeEnd;

    private String description;

    private Double latitude;

    private Double longitude;

    @ManyToOne(cascade = CascadeType.ALL)
    private Ong ong;

    @OneToMany(mappedBy = "action")
    private List<ActionVoluntary> actionVoluntaries;

    @OneToMany(mappedBy = "action")
    private List<MappingAction> mappingActions;
}