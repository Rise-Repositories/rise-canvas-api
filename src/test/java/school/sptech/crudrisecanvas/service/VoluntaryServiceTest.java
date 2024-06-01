package school.sptech.crudrisecanvas.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.VoluntaryRepository;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;
import school.sptech.crudrisecanvas.unittestutils.VoluntaryMocks;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Voluntary Service")
class VoluntaryServiceTest {

    @InjectMocks
    private VoluntaryService service;

    @Mock
    private VoluntaryRepository repository;

    @Mock
    private UserService userService;

    @Nested
    @DisplayName("getVoluntary()")
    public class getVoluntary {

        @Test
        @DisplayName("V. Quando usuário pertencer à ONG, deve retornar lista de voluntários")
        public void validData() {
            Integer ongId = 1;
            String token = UserMocks.getToken();
            User loggedUser = UserMocks.getUserWithVoluntary();
            List<Voluntary> list = VoluntaryMocks.getVoluntaryList();

            Mockito.when(userService.getAccount(token)).thenReturn(loggedUser);
            Mockito.when(repository.findAllByOngId(ongId)).thenReturn(list);

            List<Voluntary> returnedList = service.getVoluntary(ongId, token);

            assertEquals(2, returnedList.size());
            assertEquals(list.get(0).getUser(), returnedList.get(0).getUser());
            assertEquals(list.get(0).getOng(), returnedList.get(0).getOng());
            assertEquals(list.get(0).getRole(), returnedList.get(0).getRole());
        }

        @Test
        @DisplayName("F. Quando usuário não pertencer à ONG, deve lançar exceção")
        public void userNoOng() {
            Integer ongId = 10;
            String token = UserMocks.getToken();
            User loggedUser = UserMocks.getUserWithVoluntary();

            Mockito.when(userService.getAccount(token)).thenReturn(loggedUser);

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.getVoluntary(ongId, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());
        }
    }

    @Nested
    @DisplayName("createVoluntary(role, ongId, userId, token)")
    public class createVoluntary1 {

        @Test
        @DisplayName("V. Dado que dados são válidos, deve salvar novo voluntário")
        public void validData() {
            Integer ongId = 1;
            Integer userId = 2;
            String token = UserMocks.getToken();
            User loggedUser = UserMocks.getUserWithVoluntary();
            User newUser = UserMocks.getUser2();
            VoluntaryRoles role = VoluntaryRoles.VOLUNTARY;
            Voluntary newVoluntary = new Voluntary(newUser, loggedUser.getVoluntary().get(0).getOng(), role);


            Mockito.when(userService.getAccount(token)).thenReturn(loggedUser);
            Mockito.when(userService.getUserById(userId)).thenReturn(newUser);
            Mockito.when(repository.save(newVoluntary)).thenReturn(newVoluntary);

            Voluntary returnedVoluntary = service.createVoluntary(role, ongId, userId, token);

            assertEquals(newVoluntary.getUser(), returnedVoluntary.getUser());
            assertEquals(newVoluntary.getOng(), returnedVoluntary.getOng());
            assertEquals(newVoluntary.getRole(), returnedVoluntary.getRole());

            Mockito.verify(repository, Mockito.times(1)).save(any());
        }

        @Test
        @DisplayName("F. Quando usuário não pertencer à ONG, deve lançar exceção")
        public void userNoOng() {
            Integer ongId = 10;
            Integer userId = 2;
            String token = UserMocks.getToken();
            User loggedUser = UserMocks.getUserWithVoluntary();
            User newUser = UserMocks.getUser2();
            VoluntaryRoles role = VoluntaryRoles.VOLUNTARY;

            Mockito.when(userService.getAccount(token)).thenReturn(loggedUser);
            Mockito.when(userService.getUserById(userId)).thenReturn(newUser);

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.createVoluntary(role, ongId, userId, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());
        }

        @Test
        @DisplayName("F. Quando usuário for apenas voluntário, deve lançar exceção")
        public void userNoOwnerOrAdmin() {
            Integer ongId = 1;
            Integer userId = 1;
            String token = UserMocks.getToken();
            User loggedUser = UserMocks.getUserWithVoluntary2();
            User newUser = UserMocks.getUser();
            VoluntaryRoles role = VoluntaryRoles.VOLUNTARY;

            Mockito.when(userService.getAccount(token)).thenReturn(loggedUser);
            Mockito.when(userService.getUserById(userId)).thenReturn(newUser);

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.createVoluntary(role, ongId, userId, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());
        }
    }

    @Nested
    @DisplayName("createVoluntary(voluntary, ongId, token)")
    public class createVoluntary2 {
        @Test
        @DisplayName("V. Dado que dados são válidos, deve salvar novo voluntário")
        public void validData() {
            Integer ongId = 1;
            String token = UserMocks.getToken();
            User loggedUser = UserMocks.getUserWithVoluntary();
            Voluntary voluntary = VoluntaryMocks.getVoluntary2();
            voluntary.setOng(null);

            Mockito.when(userService.getAccount(token)).thenReturn(loggedUser);
            Mockito.when(repository.save(voluntary)).thenReturn(voluntary);

            Voluntary returnedVoluntary = service.createVoluntary(voluntary, ongId, token);

            assertEquals(voluntary.getUser(), returnedVoluntary.getUser());
            assertEquals(voluntary.getOng(), returnedVoluntary.getOng());
            assertEquals(voluntary.getRole(), returnedVoluntary.getRole());

            Mockito.verify(repository, Mockito.times(1)).save(any());
        }

        @Test
        @DisplayName("F. Quando usuário não pertencer à ONG, deve lançar exceção")
        public void userNoOng() {
            Integer ongId = 10;
            String token = UserMocks.getToken();
            User loggedUser = UserMocks.getUserWithVoluntary();
            Voluntary voluntary = VoluntaryMocks.getVoluntary2();
            voluntary.setOng(null);

            Mockito.when(userService.getAccount(token)).thenReturn(loggedUser);

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.createVoluntary(voluntary, ongId, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());
        }

        @Test
        @DisplayName("F. Quando usuário for apenas voluntário, deve lançar exceção")
        public void userNoOwnerOrAdmin() {
            Integer ongId = 1;
            Integer userId = 1;
            String token = UserMocks.getToken();
            User loggedUser = UserMocks.getUserWithVoluntary2();
            Voluntary voluntary = VoluntaryMocks.getVoluntary();
            voluntary.setOng(null);

            Mockito.when(userService.getAccount(token)).thenReturn(loggedUser);

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.createVoluntary(voluntary, ongId, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());
        }
    }

    @Nested
    @DisplayName("createVoluntary(voluntary)")
    public class createVoluntary3 {

        @Test
        @DisplayName("V. Quando dados estiverem corretos, deve retornar associação")
        void validData() {
            Voluntary voluntary = VoluntaryMocks.getVoluntary();

            Mockito.when(repository.save(voluntary)).thenReturn(voluntary);

            Voluntary returnedVoluntary = service.createVoluntary(voluntary);

            assertEquals(returnedVoluntary.getUser(), voluntary.getUser());
            assertEquals(returnedVoluntary.getOng(), voluntary.getOng());
            assertEquals(returnedVoluntary.getRole(), voluntary.getRole());

            Mockito.verify(repository, Mockito.times(1)).save(voluntary);
        }
    }

    @Nested
    @DisplayName("updateRole()")
    public class updateRole {

        @Test
        @DisplayName("V. Quando dados são válidos, deve salvar novo role")
        public void validData() {
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary();
            Integer id = 1;
            VoluntaryRoles role = VoluntaryRoles.ADMIN;
            Voluntary voluntary = VoluntaryMocks.getVoluntary2();

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.findById(id)).thenReturn(Optional.of(voluntary));
            Mockito.when(repository.save(any())).thenReturn(voluntary);

            Voluntary returnedVoluntary = service.updateRole(role, id, token);

            assertEquals(voluntary.getUser(), returnedVoluntary.getUser());
            assertEquals(voluntary.getRole(), returnedVoluntary.getRole());
            assertEquals(voluntary.getOng(), returnedVoluntary.getOng());

            Mockito.verify(userService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).findById(id);
            Mockito.verify(repository, Mockito.times(1)).save(any());
        }

        @Test
        @DisplayName("F. Quando id não existir, deve lançar exceção")
        public void invalidId() {
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary();
            Integer id = 10;
            VoluntaryRoles role = VoluntaryRoles.VOLUNTARY;

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> service.updateRole(role, id, token)
            );

            assertEquals("Voluntário não encontrado", exception.getLocalizedMessage());

            Mockito.verify(userService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).findById(id);
        }

        @Test
        @DisplayName("F. Quando usuário não pertencer à ONG, deve lançar exceção")
        public void userNoOng() {
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary3();
            Integer id = 1;
            VoluntaryRoles role = VoluntaryRoles.ADMIN;
            Voluntary voluntary = VoluntaryMocks.getVoluntary2();

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.findById(id)).thenReturn(Optional.of(voluntary));

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.updateRole(role, id, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());

            Mockito.verify(userService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).findById(id);
        }

        @Test
        @DisplayName("F. Quando usuário for apenas voluntário, deve lançar exceção")
        public void userNotOwnerOrAdmin() {
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary2();
            Integer id = 1;
            VoluntaryRoles role = VoluntaryRoles.ADMIN;
            Voluntary voluntary = VoluntaryMocks.getVoluntary();

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.findById(id)).thenReturn(Optional.of(voluntary));

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.updateRole(role, id, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());

            Mockito.verify(userService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).findById(id);
        }
    }

    @Nested
    @DisplayName("deleteVoluntary")
    public class deleteVoluntary {

        @Test
        @DisplayName("V. Quando dados são válidos, deve chamar service")
        public void validData() {
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary();
            Integer id = 1;
            Voluntary voluntary = VoluntaryMocks.getVoluntary2();

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.findById(id)).thenReturn(Optional.of(voluntary));

            service.deleteVoluntary(id, token);

            Mockito.verify(userService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).findById(id);
            Mockito.verify(repository, Mockito.times(1)).delete(voluntary);
        }

        @Test
        @DisplayName("F. Quando id não existir, deve lançar exceção")
        public void invalidId() {
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary();
            Integer id = 10;

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> service.deleteVoluntary(id, token)
            );

            assertEquals("Voluntário não encontrado", exception.getLocalizedMessage());

            Mockito.verify(userService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).findById(id);
        }

        @Test
        @DisplayName("F. Quando usuário não pertencer à ONG, deve lançar exceção")
        public void userNoOng() {
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary3();
            Integer id = 1;
            Voluntary voluntary = VoluntaryMocks.getVoluntary2();

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.findById(id)).thenReturn(Optional.of(voluntary));

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.deleteVoluntary(id, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());

            Mockito.verify(userService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).findById(id);
        }

        @Test
        @DisplayName("F. Quando usuário for apenas voluntário, deve lançar exceção")
        public void userNotOwnerOrAdmin() {
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary2();
            Integer id = 1;
            Voluntary voluntary = VoluntaryMocks.getVoluntary();

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.findById(id)).thenReturn(Optional.of(voluntary));

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.deleteVoluntary(id, token)
            );

            assertEquals("Você não tem permissão para acessar essa ONG", exception.getLocalizedMessage());

            Mockito.verify(userService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).findById(id);
        }
    }
}