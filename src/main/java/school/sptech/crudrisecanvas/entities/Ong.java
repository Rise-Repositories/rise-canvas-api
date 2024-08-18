package school.sptech.crudrisecanvas.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import school.sptech.crudrisecanvas.utils.Converters.OngStatusConvert;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;

@Entity
@Data
public class Ong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String cnpj;

    @ManyToOne
    private Address address;

    @Convert(converter = OngStatusConvert.class)
    private OngStatus status;

    @OneToMany(mappedBy = "ong", orphanRemoval = true)
    private List<Action> actions;

    @OneToMany(mappedBy = "ong", orphanRemoval = true)
    private List<Voluntary> voluntaries;
}