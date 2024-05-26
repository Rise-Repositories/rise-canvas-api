package school.sptech.crudrisecanvas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.VoluntaryRepository;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

@Service
@RequiredArgsConstructor
public class VoluntaryService {
    private final UserService userService;
    private final VoluntaryRepository voluntaryRepository;

    public List<Voluntary> getVoluntary(Integer ongId, String token){
        User user = userService.getAccount(token);

        if(
            !user.getVoluntary()
                .stream()
                .anyMatch(v -> v.getOng().getId().equals(ongId))
        ){
            throw new ForbiddenException("Você não tem permissão para acessar essa ONG");
        }

        return voluntaryRepository.findAllByOngId();
    }

    public Voluntary createVoluntary(VoluntaryRoles role, Integer ongId, Integer userId, String token){
        User userToValid = userService.getAccount(token);

        User user = userService.getUserById(userId);

        Ong ong = userToValid.getVoluntary()
            .stream()
            .filter(v -> v.getOng().getId().equals(ongId) && v.getRole() != VoluntaryRoles.VOLUNTARY)
            .findFirst()
            .orElseThrow(
                () -> new ForbiddenException("Você não tem permissão para acessar essa ONG")
            ).getOng();

        if(user == null){
            throw new NotFoundException("Usuário não encontrado");
        }

        Voluntary voluntary = new Voluntary(user, ong, role);

        return voluntaryRepository.save(voluntary);
    }


    public Voluntary createVoluntary(Voluntary voluntary, Integer ongId, String token){
        User user = userService.getAccount(token);

        Ong ong = user.getVoluntary()
            .stream()
            .filter(v -> v.getOng().getId().equals(ongId) && v.getRole() != VoluntaryRoles.VOLUNTARY)
            .findFirst()
            .orElseThrow(
                () -> new ForbiddenException("Você não tem permissão para acessar essa ONG")
            ).getOng();

        voluntary.setOng(ong);

        return voluntaryRepository.save(voluntary);
    }

    public Voluntary createVoluntary(Voluntary voluntary){
        return  voluntaryRepository.save(voluntary);
    }

    public Voluntary updateRole(VoluntaryRoles role, Integer id, String token){
        User user = userService.getAccount(token);

        Voluntary voluntary = voluntaryRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("Voluntário não encontrado")
            );

        if(
            !user.getVoluntary()
                .stream()
                .anyMatch(v -> v.getOng().getId().equals(voluntary.getOng().getId()) && v.getRole() != VoluntaryRoles.VOLUNTARY)
        ){
            throw new ForbiddenException("Você não tem permissão para acessar essa ONG");
        }

        voluntary.setRole(role);

        return voluntaryRepository.save(voluntary);
    }

    public void deleteVoluntary(Integer id, String token){
        User user = userService.getAccount(token);

        Voluntary voluntary = voluntaryRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException("Voluntário não encontrado")
            );

        if(
            !user.getVoluntary()
                .stream()
                .anyMatch(v -> v.getOng().getId().equals(voluntary.getOng().getId()) && v.getRole() != VoluntaryRoles.VOLUNTARY)
        ){
            throw new ForbiddenException("Você não tem permissão para acessar essa ONG");
        }

        voluntaryRepository.delete(voluntary);
    }
    
}
