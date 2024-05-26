package school.sptech.crudrisecanvas.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sptech.crudrisecanvas.utils.Converters.VoluntaryRolesConvert;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Entity
@Data
@NoArgsConstructor
public class Voluntary {
    public Voluntary(User user, Ong ong, VoluntaryRoles role) {
        this.user = user;
        this.ong = ong;
        this.role = role;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Ong ong;

    @Convert(converter = VoluntaryRolesConvert.class)
    private VoluntaryRoles role;

    @OneToMany(mappedBy = "voluntary")
    private List<ActionVoluntary> actions;
}
