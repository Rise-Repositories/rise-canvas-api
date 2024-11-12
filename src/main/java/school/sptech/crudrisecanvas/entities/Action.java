package school.sptech.crudrisecanvas.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.ManyToMany;
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

    private Double radius;

    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    private Ong ong;

    @OneToMany(mappedBy = "action")
    private List<ActionVoluntary> actionVoluntaries;

    @OneToMany(mappedBy = "action")
    private List<MappingAction> mappingActions;

    @ManyToMany
    @JoinTable(name = "action_tags",uniqueConstraints = @UniqueConstraint(columnNames = {"action_id", "tags_id"}))
    private List<Tags> tags;
}