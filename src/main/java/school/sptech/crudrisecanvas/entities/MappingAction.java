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
    public MappingAction(Action action, Mapping mapping, int qtyServedAdults, int qtyServedChildren, boolean noDonation, boolean noPeople, String description) {
        this.action = action;
        this.mapping = mapping;
        this.qtyServedAdults = qtyServedAdults;
        this.qtyServedChildren = qtyServedChildren;
        this.noDonation = noDonation;
        this.noPeople = noPeople;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Action action;

    @ManyToOne
    private Mapping mapping;

    private int qtyServedAdults;
    private int qtyServedChildren;
    private Boolean noDonation;
    private Boolean noPeople;
    private String description;
}
