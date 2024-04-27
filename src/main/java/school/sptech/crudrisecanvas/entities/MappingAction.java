package school.sptech.crudrisecanvas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class MappingAction {

    public MappingAction(Action action, Mapping mapping, int qtyServedPeople) {
        this.action = action;
        this.mapping = mapping;
        this.qtyServedPeople = qtyServedPeople;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Action action;

    @ManyToOne
    private Mapping mapping;

    private int qtyServedPeople;
}
