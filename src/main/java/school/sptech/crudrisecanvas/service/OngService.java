package school.sptech.crudrisecanvas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.ong.OngResponseDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.exception.ConflictException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.OngRepository;
import school.sptech.crudrisecanvas.utils.Enums.OngStatus;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Service
@RequiredArgsConstructor
public class OngService {
    private final OngRepository ongRepository;
    private final UserService userService;
    private final VoluntaryService voluntaryService;
    private final AddressService addressService;

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

        userService.register(user);

        if (ong.getAddress() != null) {
            Address savedAddress = addressService.saveByCep(ong.getAddress().getCep(),
                    ong.getAddress().getNumber(),
                    ong.getAddress().getComplement());
            ong.setAddress(savedAddress);
        } else {
            ong.setAddress(null);
        }

        ongRepository.save(ong);

        voluntaryService.createVoluntary(
            new Voluntary(user, ong, VoluntaryRoles.OWNER)
        );

        return ong;
    }

    public Ong updateOng(int id, Ong ong) {
        Ong ongToUpdate = this.getOngById(id);

        if(ongRepository.existsByCnpjAndIdNot(ong.getCnpj(), id)) {
            throw new ConflictException("Já existe uma ONG com este CNPJ");
        }

        ongToUpdate.setName(ong.getName());
        ongToUpdate.setCnpj(ong.getCnpj());
        ongToUpdate.setAddress(ong.getAddress());

        return ongRepository.save(ongToUpdate);
    }
    
    public void deleteOng(int id) {
        Ong ong = this.getOngById(id);
        ongRepository.delete(ong);
    }

    public Ong changeStatus(int id, OngStatus newStatus) {
        Ong ongToUpdate = this.getOngById(id);

        ongToUpdate.setStatus(newStatus);

        return ongRepository.save(ongToUpdate);
    }
}
