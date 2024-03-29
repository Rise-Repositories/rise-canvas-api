package school.sptech.crudrisecanvas.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if(!idIsValid(id) || getUserById(id) == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if(
            user.getName() == null || 
            user.getEmail() == null ||
            user.getPassword() == null ||
            user.getCpf() == null ||
            user.getPhone() == null
        ) {
            return ResponseEntity.status(400).build();
        }
        user.setId(++nextId);
        users.add(user);
        return ResponseEntity.status(201).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id,@RequestBody User user) {
        if(!idIsValid(id) || getUserById(id) == null) {
            return ResponseEntity.status(404).build();
        }
        if(
            user.getName() == null || 
            user.getEmail() == null ||
            user.getPassword() == null ||
            user.getCpf() == null ||
            user.getPhone() == null
        ) {
            return ResponseEntity.status(400).build();
        }
        user.setId(getUserById(id).getId());
        users.set(users.indexOf(getUserById(id)), user);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        if(!idIsValid(id) || getUserById(id) == null) {
            return ResponseEntity.status(404).build();
        }
        users.remove(getUserById(id));
        return ResponseEntity.status(200).build();
    }

    public boolean idIsValid(int id) {
        return id >= 0 && id <= users.size();
    }

    public User getUserById(int id) {
        for (User user : users) {
            if(user.getId() == id) {
                return user;
            }
        }
        return null;
    }

}