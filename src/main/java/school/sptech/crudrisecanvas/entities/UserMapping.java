package school.sptech.crudrisecanvas.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class UserMapping {

    @Id
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Mapping mapping;
}
