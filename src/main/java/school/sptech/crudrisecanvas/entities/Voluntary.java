package school.sptech.crudrisecanvas.entities;


import java.util.List;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @ManyToMany
    private List<Action> actions;
}
