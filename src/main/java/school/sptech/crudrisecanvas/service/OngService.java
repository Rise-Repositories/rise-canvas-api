package school.sptech.crudrisecanvas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.ConflictException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.OngRepository;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;

@Service
@RequiredArgsConstructor
public class OngService {
    private final OngRepository ongRepository;
    private final UserService userService;

    public List<Ong> getOngs() {
        return ongRepository.findAll();
    }

    public Ong getOngById(int id) {
        Optional<Ong> ong = ongRepository.findById(id);

        if(ong.isEmpty()) {
            throw new NotFoundException("ONG não encontrada");
        }

        return ong.get();
    }

    public Ong createOng(Ong ong, User user) {
        if(ongRepository.existsByCnpj(ong.getCnpj())) {
            throw new ConflictException("CNPJ já cadastrado");
        }

        ong.setStatus(OngStatus.PENDING);

        /*
         * TODO:
         * precisa relacionar os usario e a ong
         * tem que mexer com a tabela voluntary
         * tem que setar o rule e o status
         */

        userService.register(user);
        return ongRepository.save(ong);
    }

    public Ong updateOng(int id, Ong ong) {
        Ong ongToUpdate = this.getOngById(id);

        ongToUpdate.setName(ong.getName());
        ongToUpdate.setCnpj(ong.getCnpj());
        ongToUpdate.setCep(ong.getCep());
        ongToUpdate.setDescription(ong.getDescription());
        ongToUpdate.setAddress(ong.getAddress());

        return ongRepository.save(ongToUpdate);
    }
    
    public void deleteOng(int id) {
        Ong ong = this.getOngById(id);
        ongRepository.delete(ong);
    }
}
