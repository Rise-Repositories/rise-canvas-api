package school.sptech.crudrisecanvas.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserMapping {

    public UserMapping(User user, Mapping mapping) {
        this.user = user;
        this.mapping = mapping;
    }

    @Id
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Mapping mapping;
}
