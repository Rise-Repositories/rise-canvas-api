package school.sptech.crudrisecanvas.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import school.sptech.crudrisecanvas.dtos.UserRequestDto;
import school.sptech.crudrisecanvas.dtos.UserRequestMapper;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.UserRepositary;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepositary userRepositary;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepositary.findAll();
        if(users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        Optional<User> user = userRepositary.findById(id);

        if(user.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(user.get());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserRequestDto userDto) {
        if(userRepositary.countWithEmailOrCpf(userDto.getEmail(), userDto.getCpf()) > 0) {
            return ResponseEntity.status(409).build();
        }
        
        User user = UserRequestMapper.mapToUser(userDto);

        userRepositary.save(user);

        return ResponseEntity.status(201).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id,@RequestBody @Valid User user) {
        Optional<User> userOptional = userRepositary.findById(id);

        if(userOptional.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        if(userRepositary.countWithEmailOrCpfAndDiferentId(user.getEmail(), user.getCpf(), id) > 0) {
            return ResponseEntity.status(409).build();
        }

        //Todo: Implement the update logic

        userRepositary.save(userOptional.get());

        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        if(userRepositary.countWithId(id) == 0) {
            return ResponseEntity.status(404).build();
        }
        userRepositary.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}