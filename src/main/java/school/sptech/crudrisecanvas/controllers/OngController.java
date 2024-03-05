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

import school.sptech.crudrisecanvas.Entity.Ong;

@RestController
@RequestMapping("/ong")
public class OngController {

    List<Ong> ongs = new ArrayList<Ong>();

    @GetMapping
    public ResponseEntity<List<Ong>> getUsers() {
        return ResponseEntity.status(200).body(ongs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ong> getUser(@PathVariable int id) {
        if(!idIsValid(id)) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(ongs.get(id));
    }

    @PostMapping
    public ResponseEntity<Ong> createUser(@RequestBody Ong ong) {
        if(
            ong.getName() == null || 
            ong.getEmail() == null ||
            ong.getPassword() == null ||
        ong.getOccupationArea() == null ||
            ong.getDescription() == null ||
            ong.getPhone() == null
        ) {
            return ResponseEntity.status(400).build();
        }
        ongs.add(ong);
        return ResponseEntity.status(201).body(ong);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ong> updateUser(@PathVariable int id,@RequestBody Ong ong) {
        if(
            ong.getName() == null || 
            ong.getEmail() == null ||
            ong.getPassword() == null ||
            ong.getOccupationArea() == null ||
            ong.getDescription() == null ||
            ong.getPhone() == null ||
            !idIsValid(id)
        ) {
            return ResponseEntity.status(400).build();
        }
        ongs.set(id, ong);
        return ResponseEntity.status(200).body(ong);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Ong> deleteUser(@PathVariable int id) {
        if(!idIsValid(id)) {
            return ResponseEntity.status(404).build();
        }
        ongs.remove(id);
        return ResponseEntity.status(200).build();
    }

    public boolean idIsValid(int id) {
        return id >= 0 && id < ongs.size();
    }
    

    
}
