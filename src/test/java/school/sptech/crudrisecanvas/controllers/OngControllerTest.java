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
import school.sptech.crudrisecanvas.dtos.ong.OngRequestDto;
import school.sptech.crudrisecanvas.dtos.ong.OngRequestUpdateDto;
import school.sptech.crudrisecanvas.dtos.ong.OngResponseDto;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;
import school.sptech.crudrisecanvas.entities.Ong;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.integrationtests.utils.paths.OngEnum;
import school.sptech.crudrisecanvas.integrationtests.utils.paths.UserEnum;
import school.sptech.crudrisecanvas.service.OngService;
import school.sptech.crudrisecanvas.unittestutils.OngMocks;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("ONG Controller")
class   OngControllerTest {

    @InjectMocks
    private OngController controller;
    @Mock
    OngService service;

    @Nested
    @DisplayName("getOngs()")
    public class getOngs {

        @Test
        @DisplayName("V. Quando tiver dados, deve chamar service e retornar lista")
        void tableHasData() {
            List<Ong> ongs = OngMocks.getOngList();

            Mockito.when(service.getOngs()).thenReturn(ongs);

            ResponseEntity<List<OngResponseDto>> response = controller.getOngs();
            List<OngResponseDto> returnedOngs = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, returnedOngs.size());
            assertEquals(ongs.get(0).getId(), returnedOngs.get(0).getId());
            assertEquals(ongs.get(0).getName(), returnedOngs.get(0).getName());
            assertEquals(ongs.get(0).getCnpj(), returnedOngs.get(0).getCnpj());
            assertEquals(ongs.get(0).getCep(), returnedOngs.get(0).getCep());
            assertEquals(ongs.get(0).getAddress(), returnedOngs.get(0).getAddress());

            Mockito.verify(service, Mockito.times(1)).getOngs();
        }

        @Test
        @DisplayName("V. Quando não tiver dados, deve retornar 204")
        void tableNoData() {
            List<Ong> ongs = Collections.emptyList();

            Mockito.when(service.getOngs()).thenReturn(ongs);

            ResponseEntity<List<OngResponseDto>> response = controller.getOngs();

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            Mockito.verify(service, Mockito.times(1)).getOngs();
        }

    }

    @Nested
    @DisplayName("getOng()")
    public class getOng {

        @Test
        @DisplayName("V. Quando ID existir, deve retornar a ONG")
        void validId() {
            Integer id = 1;
            Ong ong = OngMocks.getOng();

            Mockito.when(service.getOngById(id)).thenReturn(ong);

            ResponseEntity<OngResponseDto> response = controller.getOng(id);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(ong.getId(), response.getBody().getId());
            assertEquals(ong.getName(), response.getBody().getName());
            assertEquals(ong.getCnpj(), response.getBody().getCnpj());
            assertEquals(ong.getCep(), response.getBody().getCep());
            assertEquals(ong.getAddress(), response.getBody().getAddress());

            Mockito.verify(service, Mockito.times(1)).getOngById(id);
        }
    }

    @Nested
    @DisplayName("createOng()")
    public class createOng {

        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 201")
        void validData() throws Exception {

            UserRequestDto userReqDto = new UserRequestDto();
            userReqDto.setName("Marcelo Soares");
            userReqDto.setEmail("marcelo.soares@email.com");
            userReqDto.setPassword("marcelo123");
            userReqDto.setCpf("017.895.420-90");

            OngRequestDto ongReqDto = new OngRequestDto();
            ongReqDto.setName("Instituto A Corrente do Bem");
            ongReqDto.setCnpj("44.454.154/0001-29");
            ongReqDto.setCep("04446060");
            ongReqDto.setAddress("232");
            ongReqDto.setUser(userReqDto);

            Ong ong = OngMocks.getOng();
            User user = UserMocks.getUser();

            Mockito.when(service.createOng(any(), any())).thenReturn(ong);

            ResponseEntity<OngResponseDto> response = controller.createOng(ongReqDto);
            OngResponseDto returnedOng = response.getBody();

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(ong.getId(), returnedOng.getId());
            assertEquals(ong.getName(), returnedOng.getName());
            assertEquals(ong.getCnpj(), returnedOng.getCnpj());
            assertEquals(ong.getCep(), returnedOng.getCep());
            assertEquals(ong.getAddress(), returnedOng.getAddress());

            Mockito.verify(service, Mockito.times(1)).createOng(any(), any());
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
            @WithMockUser
            void nameIsNull() throws Exception {
                String json = """
                        {
                                "cnpj": "44.454.154/0001-29",
                                "cep": "04446060",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome vazio")
            @WithMockUser
            void nameIsEmpty() throws Exception {
                String json = """
                        {
                                "name": "",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "04446060",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome em branco")
            @WithMockUser
            void nameIsBlank() throws Exception {
                String json = """
                        {
                                "name": "         ",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "04446060",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CNPJ nulo")
            @WithMockUser
            void cnpjIsNull() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cep": "04446060",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CNPJ vazio")
            @WithMockUser
            void cnpjIsEmpty() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "",
                                "cep": "04446060",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CNPJ em branco")
            @WithMockUser
            void cnpjIsBlank() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "             ",
                                "cep": "04446060",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CNPJ inválido")
            @WithMockUser
            void invalidCnpj() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.123/0001-29",
                                "cep": "04446060",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CEP nulo")
            @WithMockUser
            void cepIsNull() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.154/0001-29",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CEP vazio")
            @WithMockUser
            void cepIsEmpty() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CEP em branco")
            @WithMockUser
            void cepIsBlank() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "        ",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CEP inválido")
            @WithMockUser
            void invalidCep() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "00000000",
                                "address": "232",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Endereço nulo")
            @WithMockUser
            void addressIsNull() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "04446060",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Endereço vazio")
            @WithMockUser
            void addressIsEmpty() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "04446060",
                                "address": "",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Endereço em branco")
            @WithMockUser
            void addressIsBlank() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "04446060",
                                "address": "    ",
                                "user": {
                                    "name": "Marcelo Soares",
                                    "email": "marcelo.soares@email.com",
                                    "password": "marcelo123",
                                    "cpf": "017.895.420-90"
                                }
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("User nulo")
            @WithMockUser
            void userIsNull() throws Exception {
                String json = """
                        {
                                "name": "Instituto A Corrente do Bem",
                                "cnpj": "44.454.154/0001-29",
                                "cep": "04446060",
                                "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path + "/auth")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("updateOng()")
    public class updateOng {

        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 200")
        void validData() throws Exception {
            Integer id = 1;

            OngRequestUpdateDto ongReqDto = new OngRequestUpdateDto();
            ongReqDto.setName("Instituto A Corrente do Bem");
            ongReqDto.setCnpj("44.454.154/0001-29");
            ongReqDto.setCep("04446060");
            ongReqDto.setAddress("232");

            Ong ong = OngMocks.getOng();

            Mockito.when(service.updateOng(eq(id), any())).thenReturn(ong);

            ResponseEntity<OngResponseDto> response = controller.updateOng(id, ongReqDto);
            OngResponseDto returnedOng = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(ong.getId(), returnedOng.getId());
            assertEquals(ong.getName(), returnedOng.getName());
            assertEquals(ong.getCnpj(), returnedOng.getCnpj());
            assertEquals(ong.getCep(), returnedOng.getCep());
            assertEquals(ong.getAddress(), returnedOng.getAddress());

            Mockito.verify(service, Mockito.times(1)).updateOng(eq(id), any());
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
            @WithMockUser
            void nameIsNull() throws Exception {
                String json = """
                        {
                            "cnpj": "44.454.154/0001-29",
                            "cep": "04446060",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome vazio")
            @WithMockUser
            void nameIsEmpty() throws Exception {
                String json = """
                        {
                            "name": "",
                            "cnpj": "44.454.154/0001-29",
                            "cep": "04446060",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome em branco")
            @WithMockUser
            void nameIsBlank() throws Exception {
                String json = """
                        {
                            "name": "           ",
                            "cnpj": "44.454.154/0001-29",
                            "cep": "04446060",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CNPJ nulo")
            @WithMockUser
            void cnpjIsNull() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cep": "04446060",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CNPJ vazio")
            @WithMockUser
            void cnpjIsEmpty() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "",
                            "cep": "04446060",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CNPJ em branco")
            @WithMockUser
            void cnpjIsBlank() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "                ",
                            "cep": "04446060",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CEP nulo")
            @WithMockUser
            void cepIsNull() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "44.454.154/0001-29",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CEP vazio")
            @WithMockUser
            void cepIsEmpty() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "44.454.154/0001-29",
                            "cep": "",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CEP vazio")
            @WithMockUser
            void cepIsBlank() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "44.454.154/0001-29",
                            "cep": "        ",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CEP inválido")
            @WithMockUser
            void invalidCep() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "44.454.154/0001-29",
                            "cep": "00000000",
                            "address": "232"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Endereço nulo")
            @WithMockUser
            void addressIsNull() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "44.454.154/0001-29",
                            "cep": "04446060"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Endereço vazio")
            @WithMockUser
            void addressIsEmpty() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "44.454.154/0001-29",
                            "cep": "04446060",
                            "address": ""
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Endereço em branco")
            @WithMockUser
            void addressIsBlank() throws Exception {
                String json = """
                        {
                            "name": "Instituto a corrente do bem",
                            "cnpj": "44.454.154/0001-29",
                            "cep": "04446060",
                            "address": "    "
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("deleteOng()")
    public class deleteOng {

        @Test
        @DisplayName("Quando id existir, deve retornar 204")
        void deleteOng() {
            Integer id = 1;
            Mockito.doNothing().when(service).deleteOng(id);

            ResponseEntity<Void> response = controller.deleteOng(id);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Mockito.verify(service, Mockito.times(1)).deleteOng(id);
        }


    }
}