package school.sptech.crudrisecanvas.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @OneToMany(mappedBy = "actions")
    private Set<ActionVoluntary> voluntaries;

    @OneToMany(mappedBy = "action")
    private List<MappingAction> mappingActions;
}