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

import school.sptech.crudrisecanvas.Entity.Designer;

@RestController
@RequestMapping("/designer")
public class DesignerController {

    List<Designer> designers = new ArrayList<Designer>();

    @GetMapping
    public ResponseEntity<List<Designer>> getUsers() {
        return ResponseEntity.status(200).body(designers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Designer> getUser(@PathVariable int id) {
        if(!idIsValid(id)) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(designers.get(id));
    }

    @PostMapping
    public ResponseEntity<Designer> createUser(@RequestBody Designer designer) {
        if(
            designer.getName() == null || 
            designer.getEmail() == null ||
            designer.getPassword() == null ||
            designer.getCpf() == null ||
            designer.getPhone() == null
        ) {
            return ResponseEntity.status(400).build();
        }
        designers.add(designer);
        return ResponseEntity.status(201).body(designer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Designer> updateUser(@PathVariable int id,@RequestBody Designer designer) {
        if(
            designer.getName() == null || 
            designer.getEmail() == null ||
            designer.getPassword() == null ||
            designer.getCpf() == null ||
            designer.getPhone() == null ||
            !idIsValid(id)
        ) {
            return ResponseEntity.status(400).build();
        }
        designers.set(id, designer);
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Designer> deleteUser(@PathVariable int id) {
        if(!idIsValid(id)) {
            return ResponseEntity.status(404).build();
        }
        designers.remove(id);
        return ResponseEntity.status(200).build();
    }

    public boolean idIsValid(int id) {
        return id >= 0 && id < designers.size();
    }

}