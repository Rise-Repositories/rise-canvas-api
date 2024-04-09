package school.sptech.crudrisecanvas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import school.sptech.crudrisecanvas.entities.User;

public interface UserRepositary extends JpaRepository<User, Integer>{
}
