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
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryOngResponseDto;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryRequestDto;
import school.sptech.crudrisecanvas.dtos.Voluntary.VoluntaryRoleRequestDto;
import school.sptech.crudrisecanvas.entities.Voluntary;
import school.sptech.crudrisecanvas.integrationtests.utils.paths.ActionEnum;
import school.sptech.crudrisecanvas.integrationtests.utils.paths.VoluntaryEnum;
import school.sptech.crudrisecanvas.service.VoluntaryService;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;
import school.sptech.crudrisecanvas.unittestutils.VoluntaryMocks;
import school.sptech.crudrisecanvas.utils.Enums.VoluntaryRoles;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Voluntary Controller")
class VoluntaryControllerTest {

    @InjectMocks
    private VoluntaryController controller;
    @Mock
    private VoluntaryService service;

    @Nested
    @DisplayName("getVoluntary()")
    public class getVoluntary {

        @Test
        @DisplayName("V. Quando tiver dados, deve chamar service e retornar 200")
        void tableHasData() {
            Integer ongId = 1;
            List<Voluntary> voluntaries = VoluntaryMocks.getVoluntaryList();
            String token = UserMocks.getToken();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);

            Mockito.when(service.getVoluntary(ongId, token)).thenReturn(voluntaries);

            ResponseEntity<List<VoluntaryOngResponseDto>> response = controller.getVoluntary(ongId, headers);
            List<VoluntaryOngResponseDto> returnedVoluntaries = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(voluntaries.get(0).getUser().getId(), returnedVoluntaries.get(0).getUser().getId());
            assertEquals(voluntaries.get(0).getRole(), returnedVoluntaries.get(0).getRole());

            Mockito.verify(service, Mockito.times(1)).getVoluntary(ongId, token);
        }

        @Test
        @DisplayName("V. Quando não tiver dados, deve chamar service e retornar 204")
        void tableNoData() {
            Integer ongId = 1;
            List<Voluntary> voluntaries = Collections.emptyList();
            String token = UserMocks.getToken();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);

            Mockito.when(service.getVoluntary(ongId, token)).thenReturn(voluntaries);

            ResponseEntity<List<VoluntaryOngResponseDto>> response = controller.getVoluntary(ongId, headers);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            Mockito.verify(service, Mockito.times(1)).getVoluntary(ongId, token);
        }
    }

    @Nested
    @DisplayName("createVoluntary(voluntary, headers, ongId)")
    public class createVoluntary1 {

        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 201")
        void validData() {
            Integer ongId = 1;
            String token = UserMocks.getToken();
            VoluntaryRequestDto voluntaryReqDto = VoluntaryMocks.getVoluntaryRequestDto();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            Voluntary voluntary = VoluntaryMocks.getVoluntary();

            Mockito.when(service.createVoluntary(any(), any(), any())).thenReturn(voluntary);

            ResponseEntity<VoluntaryOngResponseDto> response = controller.createVoluntary(voluntaryReqDto, headers, ongId);
            VoluntaryOngResponseDto returnedVoluntary = response.getBody();

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(voluntary.getId(), returnedVoluntary.getId());
            assertEquals(voluntary.getRole(), returnedVoluntary.getRole());
            assertEquals(voluntary.getUser().getId(), returnedVoluntary.getUser().getId());

            Mockito.verify(service, Mockito.times(1)).createVoluntary(any(), any(), any());
        }

        @Nested
        @SpringBootTest
        @AutoConfigureMockMvc
        @DisplayName("F. 400 - Bad Requests")
        public class badRequests {

            @Autowired
            private MockMvc mockMvc;

            @Test
            @DisplayName("Role nulo")
            @WithMockUser(username = "testUser", password = "pass123")
            void roleIsNull() throws Exception {
                Integer ongId = 1;
                String json = """
                        {
                            "user": {
                                "name": "Marcelo Soares",
                                "email": "marcelo@email.com",
                                "password": "marcelo123",
                                "cpf": "017.895.420-90"
                            }
                        }""";
                mockMvc.perform(MockMvcRequestBuilders.post(VoluntaryEnum.BY_ID.path + ongId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Role vazio")
            @WithMockUser(username = "testUser", password = "pass123")
            void roleIsEmpty() throws Exception {
                Integer ongId = 1;
                String json = """
                        {
                            "role": "",
                            "user": {
                                "name": "Marcelo Soares",
                                "email": "marcelo@email.com",
                                "password": "marcelo123",
                                "cpf": "017.895.420-90"
                            }
                        }""";
                mockMvc.perform(MockMvcRequestBuilders.post(VoluntaryEnum.BY_ID.path + ongId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Role inválido")
            @WithMockUser(username = "testUser", password = "pass123")
            void invalidRole() throws Exception {
                Integer ongId = 1;
                String json = """
                        {
                            "role": "USER",
                            "user": {
                                "name": "Marcelo Soares",
                                "email": "marcelo@email.com",
                                "password": "marcelo123",
                                "cpf": "017.895.420-90"
                            }
                        }""";
                mockMvc.perform(MockMvcRequestBuilders.post(VoluntaryEnum.BY_ID.path + ongId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("createVoluntary(role, headers, ongId, userId)")
    public class createVoluntary2 {
        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 201")
        void validData() {
            Integer ongId = 1;
            Integer userId = 1;
            String token = UserMocks.getToken();
            VoluntaryRoleRequestDto voluntaryReqDto = new VoluntaryRoleRequestDto();
            voluntaryReqDto.setRole(VoluntaryRoles.OWNER);
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            Voluntary voluntary = VoluntaryMocks.getVoluntary();

            Mockito.when(service.createVoluntary(any(), any(), any(), any())).thenReturn(voluntary);

            ResponseEntity<VoluntaryOngResponseDto> response = controller.createVoluntary(voluntaryReqDto, headers, ongId, userId);
            VoluntaryOngResponseDto returnedVoluntary = response.getBody();

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(voluntary.getId(), returnedVoluntary.getId());
            assertEquals(voluntary.getRole(), returnedVoluntary.getRole());
            assertEquals(voluntary.getUser().getId(), returnedVoluntary.getUser().getId());

            Mockito.verify(service, Mockito.times(1)).createVoluntary(any(), any(), any(), any());
        }

        @Nested
        @SpringBootTest
        @AutoConfigureMockMvc
        @DisplayName("F. 400 - Bad Requests")
        public class badRequests {

            @Autowired
            private MockMvc mockMvc;

            @Test
            @DisplayName("Role nulo")
            @WithMockUser(username = "testUser", password = "pass123")
            void roleIsNull() throws Exception {
                Integer ongId = 1;
                Integer userId = 1;
                String json = """
                        {
                            
                        }""";
                mockMvc.perform(MockMvcRequestBuilders.post(VoluntaryEnum.BY_ID.path + ongId + "/" + userId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Role vazio")
            @WithMockUser(username = "testUser", password = "pass123")
            void roleIsEmpty() throws Exception {
                Integer ongId = 1;
                Integer userId = 1;
                String json = """
                        {
                            "role": "",
                        }""";
                mockMvc.perform(MockMvcRequestBuilders.post(VoluntaryEnum.BY_ID.path + ongId + "/" + userId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Role inválido")
            @WithMockUser(username = "testUser", password = "pass123")
            void invalidRole() throws Exception {
                Integer ongId = 1;
                Integer userId = 1;
                String json = """
                        {
                            "role": "USER",
                        }""";
                mockMvc.perform(MockMvcRequestBuilders.post(VoluntaryEnum.BY_ID.path + ongId + "/" + userId)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("updateRole()")
    public class updateRole {
        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 200")
        void validData() {
            VoluntaryRoleRequestDto voluntaryReqDto = new VoluntaryRoleRequestDto();
            voluntaryReqDto.setRole(VoluntaryRoles.OWNER);
            Integer id = 1;
            String token = UserMocks.getToken();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            Voluntary voluntary = VoluntaryMocks.getVoluntary();

            Mockito.when(service.updateRole(VoluntaryRoles.OWNER, id, token)).thenReturn(voluntary);

            ResponseEntity<VoluntaryOngResponseDto> response = controller.updateRole(voluntaryReqDto, headers, id);
            VoluntaryOngResponseDto returnedVoluntary = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(voluntary.getId(), returnedVoluntary.getId());
            assertEquals(voluntary.getRole(), returnedVoluntary.getRole());
            assertEquals(voluntary.getUser().getId(), returnedVoluntary.getUser().getId());

            Mockito.verify(service, Mockito.times(1)).updateRole(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("deleteVoluntary()")
    public class deleteVoluntary {

        @Test
        @DisplayName("V. Quando id existir, deve retornar 204")
        void validId() {
            Integer id = 1;
            String token = UserMocks.getToken();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);

            Mockito.doNothing().when(service).deleteVoluntary(id, token);

            ResponseEntity<Void> response = controller.deleteVoluntary(headers, id);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }
}