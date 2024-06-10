package school.sptech.crudrisecanvas.entities;

import java.util.List;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String cep;
    private String address;

    @Convert(converter = OngStatusConvert.class)
    private OngStatus status;

    @OneToMany(mappedBy = "ong", orphanRemoval = true)
    private List<Action> actions;

    @OneToMany(mappedBy = "ong", orphanRemoval = true)
    private List<Voluntary> voluntaries;
}