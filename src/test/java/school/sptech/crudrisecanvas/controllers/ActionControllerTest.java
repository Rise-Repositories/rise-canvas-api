package school.sptech.crudrisecanvas.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import school.sptech.crudrisecanvas.dtos.action.ActionRequestDto;
import school.sptech.crudrisecanvas.dtos.action.ActionResponseDto;
import school.sptech.crudrisecanvas.entities.Action;
import school.sptech.crudrisecanvas.integrationtests.utils.paths.ActionEnum;
import school.sptech.crudrisecanvas.service.ActionService;
import school.sptech.crudrisecanvas.unittestutils.ActionMocks;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Action Controller")
class ActionControllerTest {

    @InjectMocks
    private ActionController controller;
    @Mock
    private ActionService service;

    @Nested
    @DisplayName("getActions()")
    public class getActions {

        @Test
        @DisplayName("V. Quando tiver dados, deve chamar service e retornar 200")
        void tableHasData() {
            List<Action> actions = ActionMocks.getActionList();

            Mockito.when(service.getAll()).thenReturn(actions);

            ResponseEntity<List<ActionResponseDto>> response = controller.getActions();
            List<ActionResponseDto> returnedActions = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, returnedActions.size());
            assertEquals(actions.get(0).getId(), returnedActions.get(0).getId());
            assertEquals(actions.get(0).getName(), returnedActions.get(0).getName());
            assertEquals(actions.get(0).getDescription(), returnedActions.get(0).getDescription());
            assertEquals(actions.get(0).getDatetimeStart(), returnedActions.get(0).getDatetimeStart());
            assertEquals(actions.get(0).getDatetimeEnd(), returnedActions.get(0).getDatetimeEnd());
            assertEquals(actions.get(0).getLongitude(), returnedActions.get(0).getLongitude());
            assertEquals(actions.get(0).getLatitude(), returnedActions.get(0).getLatitude());
            assertEquals(actions.get(0).getOng().getId(), returnedActions.get(0).getOng().getId());

            Mockito.verify(service, Mockito.times(1)).getAll();
        }

        @Test
        @DisplayName("V. Quando não tiver dados, deve retornar 204")
        void tableNoData() {
            List<Action> actions = Collections.emptyList();

            Mockito.when(service.getAll()).thenReturn(actions);

            ResponseEntity<List<ActionResponseDto>> response = controller.getActions();

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Mockito.verify(service, Mockito.times(1)).getAll();
        }
    }

    @Nested
    @DisplayName("getActionById()")
    public class getActionById {

        @Test
        @DisplayName("V. Quando o id existir, deve retornar a ação")
        void idExists() {
            Integer id = 1;
            Action action = ActionMocks.getAction();

            Mockito.when(service.getById(id)).thenReturn(action);

            ResponseEntity<ActionResponseDto> response = controller.getActionById(id);
            ActionResponseDto returnedAction = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());

            assertEquals(action.getId(), returnedAction.getId());
            assertEquals(action.getName(), returnedAction.getName());
            assertEquals(action.getDescription(), returnedAction.getDescription());
            assertEquals(action.getDatetimeStart(), returnedAction.getDatetimeStart());
            assertEquals(action.getDatetimeEnd(), returnedAction.getDatetimeEnd());
            assertEquals(action.getLongitude(), returnedAction.getLongitude());
            assertEquals(action.getLatitude(), returnedAction.getLatitude());
            assertEquals(action.getOng().getId(), returnedAction.getOng().getId());

            Mockito.verify(service, Mockito.times(1)).getById(id);

        }
    }

    @Nested
    @DisplayName("createAction()")
    public class createAction {

        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 201")
        void validData() throws Exception {
            ActionRequestDto actionReqDto = ActionMocks.getActionRequestDto();
            Action action = ActionMocks.getAction();
            Integer ongId = 1;
            HashMap<String, String> hashMap = new HashMap<>();

            hashMap.put("authorization", "Bearer " + UserMocks.getToken());

            Mockito.when(service.create(any(), any(), any())).thenReturn(action);

            ResponseEntity<ActionResponseDto> response = controller.createAction(actionReqDto, ongId, hashMap);
            ActionResponseDto returnedAction = response.getBody();

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(action.getId(), returnedAction.getId());
            assertEquals(action.getName(), returnedAction.getName());
            assertEquals(action.getDescription(), returnedAction.getDescription());
            assertEquals(action.getDatetimeStart(), returnedAction.getDatetimeStart());
            assertEquals(action.getDatetimeEnd(), returnedAction.getDatetimeEnd());
            assertEquals(action.getLatitude(), returnedAction.getLatitude());
            assertEquals(action.getLongitude(), returnedAction.getLongitude());

            Mockito.verify(service, Mockito.times(1)).create(any(), any(), any());
        }

        @Nested
        @SpringBootTest
        @AutoConfigureMockMvc
        @DisplayName("F. 400 - Bad Requests")
        public class badRequests {

            @Autowired
            private MockMvc mockMvc;

            @Test
            @DisplayName("Nome nulo")
            @WithMockUser(username = "testUser", password = "pass123")
            void nameIsNull() throws Exception {
                LocalDateTime start = LocalDateTime.now().plusHours(1);
                LocalDateTime end = LocalDateTime.now().plusHours(6);
                Integer ongId = 1;
                String json = """
                        {
                            "description": "Doação de cestas básicas",
                            "dateTimeStart": "%s",
                            "dateTimeEnd": "%s",
                            "longitude": -23.4,
                            "latitude": -46.5
                        }""".formatted(start.toString(), end.toString());

                mockMvc.perform(MockMvcRequestBuilders.post(ActionEnum.BY_ID.path + ongId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome vazio")
            @WithMockUser(username = "testUser", password = "pass123")
            void nameIsEmpty() throws Exception {
                LocalDateTime start = LocalDateTime.now().plusHours(1);
                LocalDateTime end = LocalDateTime.now().plusHours(6);
                Integer ongId = 1;
                String json = """
                        {
                            "name": "",
                            "description": "Doação de cestas básicas",
                            "dateTimeStart": "%s",
                            "dateTimeEnd": "%s",
                            "longitude": -23.4,
                            "latitude": -46.5
                        }""".formatted(start.toString(), end.toString());

                mockMvc.perform(MockMvcRequestBuilders.post(ActionEnum.BY_ID.path + ongId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome em branco")
            @WithMockUser(username = "testUser", password = "pass123")
            void nameIsBlank() throws Exception {
                LocalDateTime start = LocalDateTime.now().plusHours(1);
                LocalDateTime end = LocalDateTime.now().plusHours(6);
                Integer ongId = 1;
                String json = """
                        {
                            "name": "        ",
                            "description": "Doação de cestas básicas",
                            "dateTimeStart": "%s",
                            "dateTimeEnd": "%s",
                            "longitude": -23.4,
                            "latitude": -46.5
                        }""".formatted(start.toString(), end.toString());

                mockMvc.perform(MockMvcRequestBuilders.post(ActionEnum.BY_ID.path + ongId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("dateTimeStart passada")
            @WithMockUser(username = "testUser", password = "pass123")
            void dateTimeStartPast() throws Exception {
                LocalDateTime start = LocalDateTime.now().minusHours(1);
                LocalDateTime end = LocalDateTime.now().plusHours(6);
                Integer ongId = 1;
                String json = """
                        {
                            "name": "R. Jacques Gabriel, 260",
                            "description": "Doação de cestas básicas",
                            "dateTimeStart": "%s",
                            "dateTimeEnd": "%s",
                            "longitude": -23.4,
                            "latitude": -46.5
                        }""".formatted(start.toString(), end.toString());

                mockMvc.perform(MockMvcRequestBuilders.post(ActionEnum.BY_ID.path + ongId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("dateTimeEnd passada")
            @WithMockUser(username = "testUser", password = "pass123")
            void dateTimeEndPast() throws Exception {
                LocalDateTime start = LocalDateTime.now().minusHours(1);
                LocalDateTime end = LocalDateTime.now().minusHours(6);
                Integer ongId = 1;
                String json = """
                        {
                            "name": "R. Jacques Gabriel, 260",
                            "description": "Doação de cestas básicas",
                            "dateTimeStart": "%s",
                            "dateTimeEnd": "%s",
                            "longitude": -23.4,
                            "latitude": -46.5
                        }""".formatted(start.toString(), end.toString());

                mockMvc.perform(MockMvcRequestBuilders.post(ActionEnum.BY_ID.path + ongId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("updateAction()")
    public class updateAction {
        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 200")
        void validData() throws Exception {
            Integer id = 1;
            ActionRequestDto actionReqDto = ActionMocks.getActionRequestDto();
            Action action = ActionMocks.getAction();

            Mockito.when(service.update(any(), any())).thenReturn(action);

            ResponseEntity<ActionResponseDto> response = controller.updateAction(id, actionReqDto);
            ActionResponseDto returnedAction = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(action.getId(), returnedAction.getId());
            assertEquals(action.getName(), returnedAction.getName());
            assertEquals(action.getDatetimeStart(), returnedAction.getDatetimeStart());
            assertEquals(action.getDatetimeEnd(), returnedAction.getDatetimeEnd());
            assertEquals(action.getDescription(), returnedAction.getDescription());
            assertEquals(action.getLatitude(), returnedAction.getLatitude());
            assertEquals(action.getLongitude(), returnedAction.getLongitude());
            assertEquals(action.getOng().getId(), returnedAction.getOng().getId());

            Mockito.verify(service, Mockito.times(1)).update(any(), any());
        }
    }

    @Nested
    @DisplayName("deleteMapping()")
    public class deleteMapping {

        @Test
        @DisplayName("Quando id existir, deve retornar 204")
        void deleteMapping() {

            Integer id = 1;
            Mockito.doNothing().when(service).delete(id);

            ResponseEntity<Void> response = controller.deleteAction(id);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Mockito.verify(service, Mockito.times(1)).delete(id);
        }
    }

    @Test
    void addMapping() {
    }
}