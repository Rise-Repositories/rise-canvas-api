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
import school.sptech.crudrisecanvas.dtos.UserResponseDto;
import school.sptech.crudrisecanvas.dtos.UserResponseMapper;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.UserRepositary;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepositary userRepositary;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<User> users = userRepositary.findAll();
        if(users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<UserResponseDto> usersDto = UserResponseMapper.toDto(users);
        return ResponseEntity.status(200).body(usersDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable int id) {
        Optional<User> user = userRepositary.findById(id);

        if(user.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        UserResponseDto userDto = UserResponseMapper.toDto(user.get());

        return ResponseEntity.status(200).body(userDto);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userDto) {
        if(userRepositary.countWithEmailOrCpf(userDto.getEmail(), userDto.getCpf()) > 0) {
            return ResponseEntity.status(409).build();
        }
        
        User user = UserRequestMapper.toEntity(userDto);

        UserResponseDto result = UserResponseMapper.toDto(userRepositary.save(user));

        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable int id,@RequestBody @Valid UserRequestDto user) {
        Optional<User> userOptional = userRepositary.findById(id);

        if(userOptional.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        if(userRepositary.countWithEmailOrCpfAndDiferentId(user.getEmail(), user.getCpf(), id) > 0) {
            return ResponseEntity.status(409).build();
        }

        User userEntity = UserRequestMapper.toEntity(user);

        userEntity.setId(id);

        UserResponseDto result = UserResponseMapper.toDto(userRepositary.save(userEntity));

        return ResponseEntity.status(200).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if(userRepositary.countWithId(id) == 0) {
            return ResponseEntity.status(404).build();
        }
        userRepositary.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}