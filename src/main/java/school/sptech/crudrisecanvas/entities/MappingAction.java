package school.sptech.crudrisecanvas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class MappingAction {
    @Id
    @ManyToOne
    private Action action;

    @Id
    @ManyToOne
    private Mapping mapping;

    private int qtd_served_people;
}
