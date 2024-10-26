package school.sptech.crudrisecanvas.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
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

    @ManyToOne
    private Address address;
    private LocalDateTime allowedToUpdate;
    
    @OneToMany(mappedBy = "user")
    private List<Voluntary> voluntary;

    @OneToMany(mappedBy = "user")
    private List<UserMapping> mapping;

    private String photoId;
}
