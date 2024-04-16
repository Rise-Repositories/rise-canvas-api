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
import school.sptech.crudrisecanvas.dtos.OngRequestDto;
import school.sptech.crudrisecanvas.dtos.OngRequestMapper;
import school.sptech.crudrisecanvas.dtos.OngResponseDto;
import school.sptech.crudrisecanvas.dtos.OngResponseMapper;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.repositories.OngRepository;

@RestController
@RequestMapping("/ong")
public class OngController {
    @Autowired
    OngRepository ongRepository;

    @GetMapping
    public ResponseEntity<List<OngResponseDto>> getOngs() {
        List<Ong> ongs = ongRepository.findAll();
        if(ongs.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        List<OngResponseDto> result = OngResponseMapper.toDto(ongs);

        return ResponseEntity.status(200).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OngResponseDto> getOng(@PathVariable int id) {
        Optional<Ong> ong = ongRepository.findById(id);
        if(ong.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        OngResponseDto result = OngResponseMapper.toDto(ong.get());

        return ResponseEntity.status(200).body(result);
    }

    @PostMapping
    public ResponseEntity<OngResponseDto> createOng(@RequestBody @Valid OngRequestDto ong) {
        if(ongRepository.countWithEmailOrCnpj(ong.getEmail(), ong.getCnpj()) > 0){
            return ResponseEntity.status(409).build();
        }

        Ong ongEntity = OngRequestMapper.toEntity(ong);

        OngResponseDto result = OngResponseMapper.toDto(ongRepository.save(ongEntity));

        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OngResponseDto> updateOng(@PathVariable int id,@RequestBody @Valid OngRequestDto ong) {
        Optional<Ong> ongOptional = ongRepository.findById(id);
        if(ongOptional.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        Ong ongEntity = OngRequestMapper.toEntity(ong);

        ongEntity.setId(id);

        OngResponseDto result = OngResponseMapper.toDto(ongRepository.save(ongEntity));

        return ResponseEntity.status(200).body(result);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOng(@PathVariable int id) {
        if(ongRepository.countWithId(id) == 0) {
            return ResponseEntity.status(404).build();
        }
        ongRepository.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}