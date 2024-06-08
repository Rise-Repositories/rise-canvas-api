package school.sptech.crudrisecanvas.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.exception.ConflictException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.OngRepository;
import school.sptech.crudrisecanvas.unittestutils.OngMocks;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;
import school.sptech.crudrisecanvas.unittestutils.VoluntaryMocks;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ONG Service")
class OngServiceTest {

    @InjectMocks
    private OngService service;
    @Mock
    private OngRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private VoluntaryService voluntaryService;

    @Nested
    @DisplayName("getOngs()")
    public class getOngs {

        @Test
        @DisplayName("V. Quando banco possui ONGs, deve retornar lista preenchida")
        void tableHasData() {
            List<Ong> lista = OngMocks.getOngList();

            Mockito.when(repository.findAll()).thenReturn(lista);

            List<Ong> ongs = service.getOngs();

            assertFalse(ongs.isEmpty());
            assertEquals(2, ongs.size());
            assertEquals(lista.get(0).getId(), ongs.get(0).getId());
            assertEquals(lista.get(0).getName(), ongs.get(0).getName());
            assertEquals(lista.get(0).getCnpj(), ongs.get(0).getCnpj());
            assertEquals(lista.get(0).getCep(), ongs.get(0).getCep());
            assertEquals(lista.get(0).getAddress(), ongs.get(0).getAddress());
        }

        @Test
        @DisplayName("V. Quando banco esta vazio, deve retornar lista vazia")
        void emptyTable() {
            List<Ong> lista = Collections.emptyList();

            Mockito.when(repository.findAll()).thenReturn(lista);

            List<Ong> ongs = service.getOngs();

            assertTrue(ongs.isEmpty());
        }
    }

    @Nested
    @DisplayName("getOngById()")
    public class getOngById {

        @Test
        @DisplayName("V. Quando ID existir, deve retornar a ONG")
        void idExists() {
            Integer id = 1;
            Ong ong = OngMocks.getOng();

            Mockito.when(repository.findById(id)).thenReturn(Optional.of(ong));

            Ong returnedOng = service.getOngById(id);

            assertEquals(ong.getId(), returnedOng.getId());
            assertEquals(ong.getName(), returnedOng.getName());
            assertEquals(ong.getCnpj(), returnedOng.getCnpj());
            assertEquals(ong.getCep(), returnedOng.getCep());
            assertEquals(ong.getAddress(), returnedOng.getAddress());
        }

        @Test
        @DisplayName("F. Quando ID não existir, deve lançar NotFoundException")
        void emptyTable() {
            Integer id = 10;

            Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> service.getOngById(id)
            );

            assertEquals("ONG não encontrada", exception.getLocalizedMessage());
        }
    }

    @Nested
    @DisplayName("createOng()")
    public class createOng {

        @Test
        @DisplayName("V. Quando dados forem corretos, deve criar a ONG, usuário e associação")
        void validData() {
            Ong ong = OngMocks.getOng();
            User user = UserMocks.getUser();
            Voluntary voluntary = VoluntaryMocks.getVoluntary();
            voluntary.setId(null);

            Mockito.when(repository.existsByCnpj(ong.getCnpj())).thenReturn(false);
            Mockito.when(voluntaryService.createVoluntary(voluntary)).thenReturn(voluntary);

            Ong returnedOng = service.createOng(ong, user);

            assertEquals(ong.getName(), returnedOng.getName());
            assertEquals(ong.getCnpj(), returnedOng.getCnpj());
            assertEquals(ong.getCep(), returnedOng.getCep());
            assertEquals(ong.getAddress(), returnedOng.getAddress());

            Mockito.verify(repository, Mockito.times(1)).save(ong);
            Mockito.verify(userService, Mockito.times(1)).register(user);
            Mockito.verify(voluntaryService, Mockito.times(1)).createVoluntary(voluntary);
        }

        @Test
        @DisplayName("V. Quando CNPJ já existir, deve lançar ConflictException")
        void repeatCnpj() {
            Ong ong = OngMocks.getOng();
            User user = UserMocks.getUser();

            Mockito.when(repository.existsByCnpj(ong.getCnpj())).thenReturn(true);

            ConflictException exception = assertThrows(
                    ConflictException.class,
                    () -> service.createOng(ong, user)
            );

            assertEquals("CNPJ já cadastrado", exception.getLocalizedMessage());

        }
    }

    @Nested
    @DisplayName("updateOng()")
    public class updateOng {

        @Test
        @DisplayName("V. Quando ONG possui dados válidos, atualiza ONG")
        void validData() {
            Integer id = 1;
            Ong currentOng = OngMocks.getOng();
            Ong newOng = OngMocks.getOng();
            newOng.setName("Teto");
            newOng.setCnpj("76852512000148");
            newOng.setCep("01223010");
            newOng.setAddress("56");

            OngService spyService = Mockito.spy(service);

            Mockito.doReturn(currentOng).when(spyService).getOngById(id);
            Mockito.when(repository.existsByCnpjAndIdNot(newOng.getCnpj(), id)).thenReturn(false);
            Mockito.when(repository.save(newOng)).thenReturn(newOng);

            Ong returnedOng = spyService.updateOng(id, newOng);

            assertEquals(currentOng.getId(), returnedOng.getId());
            assertEquals(newOng.getName(), returnedOng.getName());
            assertEquals(newOng.getCnpj(), returnedOng.getCnpj());
            assertEquals(newOng.getCep(), returnedOng.getCep());
            assertEquals(newOng.getAddress(), returnedOng.getAddress());

            Mockito.verify(repository, Mockito.times(1)).existsByCnpjAndIdNot(newOng.getCnpj(), id);
        }

        @Test
        @DisplayName("F. QUando CNPJ já existir em outra ONG, deve lançar ConflictException")
        void cnpjAlreadyExists() {
            Integer id = 1;
            Ong currentOng = OngMocks.getOng();
            Ong newOng = OngMocks.getOng();
            newOng.setName("Teto");
            newOng.setCnpj("76852512000148");
            newOng.setCep("01223010");
            newOng.setAddress("56");

            OngService spyService = Mockito.spy(service);

            Mockito.doReturn(currentOng).when(spyService).getOngById(id);
            Mockito.when(repository.existsByCnpjAndIdNot(newOng.getCnpj(), id)).thenReturn(true);

            ConflictException exception = assertThrows(
                    ConflictException.class,
                    () -> spyService.updateOng(id, newOng)
            );

            assertEquals("Já existe uma ONG com este CNPJ", exception.getLocalizedMessage());

            Mockito.verify(repository, Mockito.times(1)).existsByCnpjAndIdNot(newOng.getCnpj(), id);
        }
    }

    @Nested
    @DisplayName("deleteOng()")
    public class deleteOng {
        @Test
        @DisplayName("V. Quando id existe, deve executar delete()")
        void validId() {
            Integer id = 1;
            Ong ong = OngMocks.getOng();

            OngService spyService = Mockito.spy(service);

            Mockito.doReturn(ong).when(spyService).getOngById(id);

            spyService.deleteOng(id);

            Mockito.verify(repository, Mockito.times(1)).delete(ong);
            Mockito.verify(spyService, Mockito.times(1)).getOngById(id);
        }

        @Test
        @DisplayName("F. Quando id não existe, deve lançar NotFoundException")
        void invalidId() {
            Integer id = 10;

            OngService spyService = Mockito.spy(service);

            Mockito.doThrow(new NotFoundException("ONG não encontrada"))
                    .when(spyService).getOngById(id);

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> spyService.deleteOng(id)
            );

            assertEquals("ONG não encontrada", exception.getLocalizedMessage());
            Mockito.verify(spyService, Mockito.times(1)).getOngById(id);
        }
    }
}