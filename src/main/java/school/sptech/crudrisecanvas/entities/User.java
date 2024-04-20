package school.sptech.crudrisecanvas.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String cpf;
    private String ip;

    @OneToMany(mappedBy = "user")
    private List<Mapping> mapping;

    @OneToMany(mappedBy = "user")
    private List<Voluntary> voluntary;

    public void addMapping(Mapping mapping){
        this.mapping.add(mapping);
    }
}
