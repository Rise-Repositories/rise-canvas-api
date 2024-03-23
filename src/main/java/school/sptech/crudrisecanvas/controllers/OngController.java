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
    int nextId;

    @GetMapping
    public ResponseEntity<List<Ong>> getOngs() {
        if(ongs.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(ongs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ong> getOng(@PathVariable int id) {
        if(!idIsValid(id)) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(getOngById(id));
    }

    @PostMapping
    public ResponseEntity<Ong> createOng(@RequestBody Ong ong) {
        if(
            ong.getName() == null || 
            ong.getEmail() == null ||
            ong.getPassword() == null ||
            ong.getCnpj() == null
        ) {
            return ResponseEntity.status(400).build();
        }
        ong.setId(++nextId);
        ongs.add(ong);
        return ResponseEntity.status(201).body(ong);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ong> updateOng(@PathVariable int id,@RequestBody Ong ong) {
        if(!idIsValid(id) || getOngById(id) == null) {
            return ResponseEntity.status(404).build();
        }
        if(
            ong.getName() == null || 
            ong.getEmail() == null ||
            ong.getPassword() == null ||
            ong.getCnpj() == null
        ) {
            return ResponseEntity.status(400).build();
        }
        ong.setId(getOngById(id).getId());
        ongs.set(ongs.indexOf(getOngById(id)), ong);
        return ResponseEntity.status(200).body(ong);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Ong> deleteOng(@PathVariable int id) {
        if(!idIsValid(id) || getOngById(id) == null) {
            return ResponseEntity.status(404).build();
        }
        ongs.remove(getOngById(id));
        return ResponseEntity.status(200).build();
    }

    private boolean idIsValid(int id) {
        return id >= 0 && id <= ongs.size();
    }

    private Ong getOngById(int id) {
        for ( Ong ong : ongs ) {
            if(ong.getId() == id) {
                return ong;
            }
        }
        return null;
    }


}
