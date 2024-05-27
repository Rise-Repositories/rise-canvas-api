package school.sptech.crudrisecanvas.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class ActionVoluntary {

    @Id
    private int id;

    @ManyToOne
    private Action action;

    @ManyToOne
    private Voluntary voluntary;

}
