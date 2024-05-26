package school.sptech.crudrisecanvas.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.MappingActionRepository;
import school.sptech.crudrisecanvas.repositories.MappingRepository;
import school.sptech.crudrisecanvas.unittestutils.MappingMocks;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mapping Service")
class MappingServiceTest {

    @InjectMocks
    private MappingService service;

    @Mock
    private MappingActionRepository mappingActionRepository;
    @Mock
    private MappingRepository mappingRepository;
    @Mock
    private UserService userService;


    @Nested
    @DisplayName("getMappings()")
    public class getMappings {

        @Test
        @DisplayName("V. Quando banco possui mapeamentos, deve retornar lista preenchida")
        void tableHasData() {
            List<Mapping> lista = MappingMocks.getMappingList();

            Mockito.when(mappingRepository.findAll()).thenReturn(lista);

            List<Mapping> mappings = service.getMappings();

            assertFalse(mappings.isEmpty());
            assertEquals(2, mappings.size());
            assertEquals(lista.get(0).getId(), mappings.get(0).getId());
            assertEquals(lista.get(0).getQtyAdults(), mappings.get(0).getQtyAdults());
            assertEquals(lista.get(0).getQtyChildren(), mappings.get(0).getQtyChildren());
            assertEquals(lista.get(0).getReferencePoint(), mappings.get(0).getReferencePoint());
            assertEquals(lista.get(0).getHasDisorders(), mappings.get(0).getHasDisorders());
            assertEquals(lista.get(0).getDescription(), mappings.get(0).getDescription());
            assertEquals(lista.get(0).getLatitude(), mappings.get(0).getLatitude());
            assertEquals(lista.get(0).getLongitude(), mappings.get(0).getLongitude());
            assertEquals(lista.get(0).getStatus(), mappings.get(0).getStatus());
            assertEquals(lista.get(0).getDate(), mappings.get(0).getDate());
            assertEquals(lista.get(0).getUsers().get(0).getName(), mappings.get(0).getUsers().get(0).getName());
        }

        @Test
        @DisplayName("V. Quando banco está vazio, deve retornar lista vazia")
        void emptyTable() {
            List<Mapping> lista = Collections.emptyList();

            Mockito.when(mappingRepository.findAll()).thenReturn(lista);

            List<Mapping> mappings = service.getMappings();

            assertTrue(mappings.isEmpty());
        }
    }

    @Nested
    @DisplayName("getMappingById()")
    public class getMappingById {

        @Test
        @DisplayName("V. Quando ID existir, deve retornar uma ação")
        void idExists() {
            Integer id = 1;
            Mapping mapping = MappingMocks.getMapping();

            Mockito.when(mappingRepository.findById(id)).thenReturn(Optional.of(mapping));

            Mapping returnedMapping = service.getMappingById(id);

            assertEquals(mapping.getId(), returnedMapping.getId());
            assertEquals(mapping.getQtyAdults(), returnedMapping.getQtyAdults());
            assertEquals(mapping.getQtyChildren(), returnedMapping.getQtyChildren());
            assertEquals(mapping.getReferencePoint(), returnedMapping.getReferencePoint());
            assertEquals(mapping.getHasDisorders(), returnedMapping.getHasDisorders());
            assertEquals(mapping.getDescription(), returnedMapping.getDescription());
            assertEquals(mapping.getLatitude(), returnedMapping.getLatitude());
            assertEquals(mapping.getLongitude(), returnedMapping.getLongitude());
            assertEquals(mapping.getStatus(), returnedMapping.getStatus());
            assertEquals(mapping.getDate(), returnedMapping.getDate());
            assertEquals(mapping.getUsers().get(0).getName(), returnedMapping.getUsers().get(0).getName());
        }

        @Test
        @DisplayName("F. Quando ID não existir, deve lançar NotFoundException")
        void idDoesntExists() {
            Integer id = 10;

            Mockito.when(mappingRepository.findById(id)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> service.getMappingById(id)
            );

            assertEquals("Mapeamento não encontrado", exception.getLocalizedMessage());
        }
    }

    @Nested
    @DisplayName("createMapping()")
    public class createMapping {

        @Test
        @DisplayName("V. Quando dados forem válidos, deve criar um mapeamento")
        void validData() {
            Mapping mapping = MappingMocks.getCreationMapping();
            User user = UserMocks.getUser();
            String token = UserMocks.getToken();

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(mappingRepository.save(mapping)).thenReturn(mapping);

            Mapping returnedMapping = service.createMapping(mapping, token);

            assertEquals(mapping.getId(), returnedMapping.getId());
            assertEquals(mapping.getQtyAdults(), returnedMapping.getQtyAdults());
            assertEquals(mapping.getQtyChildren(), returnedMapping.getQtyChildren());
            assertEquals(mapping.getReferencePoint(), returnedMapping.getReferencePoint());
            assertEquals(mapping.getHasDisorders(), returnedMapping.getHasDisorders());
            assertEquals(mapping.getDescription(), returnedMapping.getDescription());
            assertEquals(mapping.getLatitude(), returnedMapping.getLatitude());
            assertEquals(mapping.getLongitude(), returnedMapping.getLongitude());
            assertEquals(MappingStatus.ACTIVE, returnedMapping.getStatus());
            assertEquals(mapping.getDate(), returnedMapping.getDate());
            assertEquals(user.getName(), returnedMapping.getUsers().get(0).getName());

            Mockito.verify(mappingRepository, Mockito.times(1)).save(any());
        }
    }

    @Nested
    @DisplayName("updateMapping")
    public class updateMapping {

        @Test
        @DisplayName("V. Quando mapeamento possui dados válidos, atualiza mapeamento")
        void validData() {
            Integer id = 1;
            Mapping currentMapping = MappingMocks.getMapping();
            Mapping newMapping = MappingMocks.getMapping2();
            newMapping.setId(1);

            MappingService spyService = Mockito.spy(service);

            Mockito.doReturn(currentMapping).when(spyService).getMappingById(id);
            Mockito.when(mappingRepository.save(currentMapping)).thenReturn(currentMapping);

            Mapping returnedMapping = spyService.updateMapping(id, newMapping);

            assertEquals(id, returnedMapping.getId());
            assertEquals(newMapping.getQtyAdults(), returnedMapping.getQtyAdults());
            assertEquals(newMapping.getQtyChildren(), returnedMapping.getQtyChildren());
            assertEquals(newMapping.getReferencePoint(), returnedMapping.getReferencePoint());
            assertEquals(newMapping.getHasDisorders(), returnedMapping.getHasDisorders());
            assertEquals(newMapping.getDescription(), returnedMapping.getDescription());
            assertEquals(newMapping.getLatitude(), returnedMapping.getLatitude());
            assertEquals(newMapping.getLongitude(), returnedMapping.getLongitude());
            assertEquals(MappingStatus.ACTIVE, returnedMapping.getStatus());
            assertEquals(newMapping.getDate(), returnedMapping.getDate());
            assertEquals(newMapping.getUsers().get(0).getName(), returnedMapping.getUsers().get(0).getName());

            Mockito.verify(mappingRepository, Mockito.times(1)).save(any());
            Mockito.verify(spyService, Mockito.times(1)).getMappingById(id);
        }
    }

    @Nested
    @DisplayName("deleteMapping()")
    public class deleteMapping {
        @Test
        @DisplayName("V. Quando id existe, deve executar delete()")
        void validId() {
            Integer id = 1;
            Mapping mapping = MappingMocks.getMapping();

            MappingService spyService = Mockito.spy(service);

            Mockito.doReturn(mapping).when(spyService).getMappingById(id);

            spyService.deleteMapping(id);

            Mockito.verify(spyService, Mockito.times(1)).getMappingById(id);
            Mockito.verify(mappingActionRepository, Mockito.times(1)).deleteAllByMappingId(id);
        }
    }

    @Nested
    @DisplayName("addUser()")
    public class addUser {

    }
}