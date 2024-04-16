package school.sptech.crudrisecanvas.entities;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Voluntary {
    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Ong ong;
    
    private String role;

    @ManyToMany
    private List<Action> actions;
}
