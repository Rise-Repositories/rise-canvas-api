package school.sptech.crudrisecanvas.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import school.sptech.crudrisecanvas.Entity.User;

@RestController
@RequestMapping("/user")
public class UserController {

    List<User> users = new ArrayList<User>();
    int nextId;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        if(users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        Optional<User> user = getUserById(id);

        if(user.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(user.get());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        if(emailExists(user.getEmail()) || cpfExists(user.getCpf())){
            return ResponseEntity.status(409).build();
        }

        user.setId(++nextId);
        users.add(user);
        return ResponseEntity.status(201).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id,@RequestBody @Valid User user) {
        Optional<User> userOptional = getUserById(id);

        if(userOptional.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        if(emailExists(user.getEmail(), id) || cpfExists(user.getCpf(), id)){
            return ResponseEntity.status(409).build();
        }
        
        int index = users.indexOf(userOptional.get());

        user.setId(userOptional.get().getId());
        users.set(index, user);

        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        Optional<User> user = getUserById(id);
        if(user.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        users.remove(user.get());
        return ResponseEntity.status(204).build();
    }

    public Optional<User> getUserById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    public boolean emailExists(String email) {
        return 
            users.stream()
                .anyMatch(
                    user -> user.getEmail().equals(email)
                );
    }

    public boolean emailExists(String email, int id) {
        return 
            users.stream()
                .anyMatch(
                    user -> user.getEmail().equals(email) && user.getId() != id
                );
    }

    public boolean cpfExists(String cpf) {
        return 
            users.stream()
                .anyMatch(
                    user -> user.getCpf().equals(cpf)
                );
    }

    public boolean cpfExists(String cpf, int id) {
        return 
            users.stream()
                .anyMatch(
                    user -> user.getCpf().equals(cpf) && user.getId() != id
                );
    }

}