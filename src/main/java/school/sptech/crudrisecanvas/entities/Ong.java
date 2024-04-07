package school.sptech.crudrisecanvas.entities;

import java.util.Set;

import org.hibernate.validator.constraints.br.CNPJ;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;


    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    @NotBlank
    @CNPJ
    private String cnpj;

    private String description;

    @NotBlank
    @Size(min = 8, max = 8)
    private String cep;
    
    @NotBlank
    private String address;

    @Convert(converter = OngStatusConvert.class)
    private OngStatus status;

    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Action> actions;

    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Voluntary> voluntaries;
}