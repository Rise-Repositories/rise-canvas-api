package school.sptech.crudrisecanvas.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MappingAction {
    @Id
    @ManyToOne
    private Action action;

    @Id
    @ManyToOne
    private Mapping mapping;

    private int qtd_served_people;
}
