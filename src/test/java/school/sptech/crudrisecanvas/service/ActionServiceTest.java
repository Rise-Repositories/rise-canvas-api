package school.sptech.crudrisecanvas.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.crudrisecanvas.entities.*;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.ActionRepository;
import school.sptech.crudrisecanvas.repositories.MappingActionRepository;
import school.sptech.crudrisecanvas.unittestutils.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("Action Service")
class ActionServiceTest {

    @InjectMocks
    private ActionService service;
    @Mock
    private UserService userService;
    @Mock
    private MappingService mappingService;
    @Mock
    private ActionRepository repository;
    @Mock
    private MappingActionRepository mappingActionRepository;

    @Nested
    @DisplayName("getAll()")
    public class getAll {

        @Test
        @DisplayName("V. Quando banco possui Ações, deve retornar lista preenchida")
        void tableHasData() {
            List<Action> lista = ActionMocks.getActionList();

            Mockito.when(repository.findAll()).thenReturn(lista);

            List<Action> actions = service.getAll();
            assertFalse(actions.isEmpty());
            assertEquals(2, actions.size());
            assertEquals(lista.get(0).getId(), actions.get(0).getId());
            assertEquals(lista.get(0).getName(), actions.get(0).getName());
            assertEquals(lista.get(0).getDatetimeStart(), actions.get(0).getDatetimeStart());
            assertEquals(lista.get(0).getDatetimeEnd(), actions.get(0).getDatetimeEnd());
            assertEquals(lista.get(0).getDescription(), actions.get(0).getDescription());
            assertEquals(lista.get(0).getLatitude(), actions.get(0).getLatitude());
            assertEquals(lista.get(0).getLongitude(), actions.get(0).getLongitude());
            assertEquals(lista.get(0).getOng(), actions.get(0).getOng());
            assertEquals(lista.get(0).getActionVoluntaries(), actions.get(0).getActionVoluntaries());
        }

        @Test
        @DisplayName("V. Quando banco está vazio, deve retornar lista vazia")
        void emptyTable() {
            List<Action> lista = Collections.emptyList();

            Mockito.when(repository.findAll()).thenReturn(lista);

            List<Action> actions = service.getAll();

            assertTrue(actions.isEmpty());
        }
    }

    @Nested
    @DisplayName("getById()")
    public class getById {

        @Test
        @DisplayName("V. Quando ID existir, deve retornar uma ação")
        void idExists() {
            Integer id = 1;
            Action action = ActionMocks.getAction();

            Mockito.when(repository.findById(id)).thenReturn(Optional.of(action));

            Action returnedAction = service.getById(id);

            assertEquals(action.getId(), returnedAction.getId());
            assertEquals(action.getName(), returnedAction.getName());
            assertEquals(action.getDatetimeStart(), returnedAction.getDatetimeStart());
            assertEquals(action.getDatetimeEnd(), returnedAction.getDatetimeEnd());
            assertEquals(action.getDescription(), returnedAction.getDescription());
            assertEquals(action.getLongitude(), returnedAction.getLongitude());
            assertEquals(action.getLatitude(), returnedAction.getLatitude());
            assertEquals(action.getOng(), returnedAction.getOng());
            assertEquals(action.getActionVoluntaries(), returnedAction.getActionVoluntaries());
        }

        @Test
        @DisplayName("F. Quando ID não existir, deve lançar NotFoundException")
        void idDoesntExists() {
            Integer id = 10;

            Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> service.getById(id)
            );

            assertEquals("Ação não encontrada", exception.getLocalizedMessage());
        }
    }

    @Nested
    @DisplayName("create()")
    public class create {
        @Test
        @DisplayName("V. Quando dados forem válidos, deve criar uma ação")
        void validData() {
            Integer ongId = 1;
            String token = UserMocks.getToken();
            User user = UserMocks.getUserWithVoluntary();
            Action action = ActionMocks.getAction();

            Mockito.when(userService.getAccount(token)).thenReturn(user);
            Mockito.when(repository.save(action)).thenReturn(action);

            Action returnedAction = service.create(action, ongId, token);

            assertEquals(action.getName(), returnedAction.getName());
            assertEquals(action.getDatetimeStart(), returnedAction.getDatetimeStart());
            assertEquals(action.getDatetimeEnd(), returnedAction.getDatetimeEnd());
            assertEquals(action.getDescription(), returnedAction.getDescription());
            assertEquals(action.getLongitude(), returnedAction.getLongitude());
            assertEquals(action.getLatitude(), returnedAction.getLatitude());
            assertEquals(action.getOng(), returnedAction.getOng());
            assertEquals(action.getActionVoluntaries(), returnedAction.getActionVoluntaries());

            Mockito.verify(repository, Mockito.times(1)).save(action);
        }

        @Test
        @DisplayName("F. Quando usuário não for voluntário da ONG, deve lançar ForbiddenException")
        void noVoluntary() {
            Integer ongId = 1;
            String token = UserMocks.getToken();
            User user = UserMocks.getUser();
            user.setVoluntary(Collections.emptyList());
            Action action = ActionMocks.getAction();

            Mockito.when(userService.getAccount(token)).thenReturn(user);

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> service.create(action, ongId, token)
            );

            assertEquals("Você não tem permissão para criar essa ação", exception.getLocalizedMessage());
        }
    }

    @Nested
    @DisplayName("update()")
    public class update {

        @Test
        @DisplayName("V. Quando ação possui dados válidos, atualiza ação")
        void validData() {
            Integer id = 1;
            Action currentAction = ActionMocks.getAction();
            Action newAction = ActionMocks.getAction();
            newAction.setName("R. Campo Grande, 62");
            newAction.setDatetimeStart(LocalDateTime.of(2024, 3, 7, 8, 10, 0));
            newAction.setDatetimeEnd(LocalDateTime.of(2024, 3, 7, 15, 45, 0));
            newAction.setDescription("Doação de comida e itens de higiene pessoal");
            newAction.setLatitude(-23.531682);
            newAction.setLongitude(-46.721047);

            ActionService spyService = Mockito.spy(service);

            Mockito.doReturn(currentAction).when(spyService).getById(id);
            Mockito.when(repository.save(any())).thenReturn(newAction);

            Action returnedAction = spyService.update(newAction, id);

            assertEquals(currentAction.getId(), returnedAction.getId());
            assertEquals(newAction.getName(), returnedAction.getName());
            assertEquals(newAction.getDatetimeStart(), returnedAction.getDatetimeStart());
            assertEquals(newAction.getDatetimeEnd(), returnedAction.getDatetimeEnd());
            assertEquals(newAction.getDescription(), returnedAction.getDescription());
            assertEquals(newAction.getLatitude(), returnedAction.getLatitude());
            assertEquals(newAction.getLongitude(), returnedAction.getLongitude());

            Mockito.verify(repository, Mockito.times(1)).save(any());
        }

    }

    @Nested
    @DisplayName("delete()")
    public class delete {

        @Test
        @DisplayName("V. Quando id existe, deve executar delete()")
        void validId() {
            Integer id = 1;
            Action action = ActionMocks.getAction();

            ActionService spyService = Mockito.spy(service);

            Mockito.doReturn(action).when(spyService).getById(id);

            spyService.delete(id);

            Mockito.verify(repository, Mockito.times(1)).delete(action);
            Mockito.verify(spyService, Mockito.times(1)).getById(id);
        }

        @Test
        @DisplayName("F. Quando id não existe, deve lançar NotFoundException")
        void invalidId() {
            Integer id = 10;

            ActionService spyService = Mockito.spy(service);

            Mockito.doThrow(new NotFoundException("Ação não encontrada"))
                    .when(spyService).getById(id);

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> spyService.delete(id)
            );

            assertEquals("Ação não encontrada", exception.getLocalizedMessage());
            Mockito.verify(spyService, Mockito.times(1)).getById(id);
        }

    }

    @Nested
    @DisplayName("addMapping()")
    public class addMapping {

        @Test
        @DisplayName("V. Quando dados são validos, salva a MappingAction")
        void validData() {
            Integer actionId = 1;
            Integer mappingId = 1;
            Action action = ActionMocks.getAction();
            Mapping mapping = MappingMocks.getMapping();
            MappingAction mappingAction = MappingActionMocks.getMappingAction();

            ActionService spyService = Mockito.spy(service);

            Mockito.doReturn(action).when(spyService).getById(actionId);
            Mockito.when(mappingService.getMappingById(mappingId)).thenReturn(mapping);
            Mockito.when(mappingActionRepository.save(mappingAction)).thenReturn(mappingAction);

            MappingAction returnedMappingAction = spyService.addMapping(actionId, mappingId, mappingAction);

            assertEquals(mappingAction.getQtyServedAdults(), returnedMappingAction.getQtyServedAdults());
            assertEquals(mappingAction.getQtyServedChildren(), returnedMappingAction.getQtyServedChildren());
            assertEquals(mappingAction.getAction(), returnedMappingAction.getAction());
            assertEquals(mappingAction.getMapping(), returnedMappingAction.getMapping());
            assertEquals(mappingAction.getNoDonation(), returnedMappingAction.getNoDonation());
            assertEquals(mappingAction.getNoPeople(), returnedMappingAction.getNoPeople());
            assertEquals(mappingAction.getDescription(), returnedMappingAction.getDescription());

            Mockito.verify(spyService, Mockito.times(1)).getById(actionId);
            Mockito.verify(mappingService, Mockito.times(1)).getMappingById(mappingId);
            Mockito.verify(mappingActionRepository, Mockito.times(1)).save(mappingAction);


        }

    }
}