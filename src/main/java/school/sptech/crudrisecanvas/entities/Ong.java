package school.sptech.crudrisecanvas.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import school.sptech.crudrisecanvas.Utils.Converters.OngStatusConvert;
import school.sptech.crudrisecanvas.Utils.Enums.OngStatus;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Ong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String password;
    private String cnpj;
    private String description;
    private String cep;
    private String address;

    @Convert(converter = OngStatusConvert.class)
    private OngStatus status;

    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Action> actions;

    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Voluntary> voluntaries;

    public void addAction(Action action) {
        this.actions.add(action);
    }
}