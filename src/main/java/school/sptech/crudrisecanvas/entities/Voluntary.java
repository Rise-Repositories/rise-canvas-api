package school.sptech.crudrisecanvas.entities;


import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ong getOng() {
        return ong;
    }

    public void setOng(Ong ong) {
        this.ong = ong;
    }   

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
