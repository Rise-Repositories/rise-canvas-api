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
        Optional<Ong> ong = getOngById(id);
        if(ong.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(ong.get());
    }

    @PostMapping
    public ResponseEntity<Ong> createOng(@RequestBody @Valid Ong ong) {
        if(emailExists(ong.getEmail()) || cnpjExists(ong.getCnpj())){
            return ResponseEntity.status(409).build();
        }
        ong.setId(++nextId);
        ongs.add(ong);
        return ResponseEntity.status(201).body(ong);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ong> updateOng(@PathVariable int id,@RequestBody @Valid Ong ong) {
        Optional<Ong> ongOptional = getOngById(id);
        if(ongOptional.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        if(emailExists(ong.getEmail(), id) || cnpjExists(ong.getCnpj(), id)){
            return ResponseEntity.status(409).build();
        }

        int index = ongs.indexOf(ongOptional.get());

        ong.setId(index);
        ongs.set(index, ong);
        return ResponseEntity.status(200).body(ong);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Ong> deleteOng(@PathVariable int id) {
        Optional<Ong> ong = getOngById(id);
        if(ong.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        ongs.remove(ong.get());
        return ResponseEntity.status(204).build();
    }

    private Optional<Ong> getOngById(int id) {
        return 
            ongs.stream()
                .filter(
                    user -> user.getId() == id
                )
                .findFirst();
    }

    public boolean emailExists(String email) {
        return 
            ongs.stream()
                .anyMatch(
                    user -> user.getEmail().equals(email)
                );
    }

    public boolean emailExists(String email, int id) {
        return 
            ongs.stream()
                .anyMatch(
                    user -> user.getEmail().equals(email) && user.getId() != id
                );
    }

    public boolean cnpjExists(String cpf) {
        return 
            ongs.stream()
                .anyMatch(
                    user -> user.getCnpj().equals(cpf)
                );
    }

    public boolean cnpjExists(String cpf, int id) {
        return 
            ongs.stream()
                .anyMatch(
                    user -> user.getCnpj().equals(cpf) && user.getId() != id
                );
    }

}
