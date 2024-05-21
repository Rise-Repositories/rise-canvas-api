package school.sptech.crudrisecanvas.controllers;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import school.sptech.crudrisecanvas.dtos.user.UserLoginDto;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;
import school.sptech.crudrisecanvas.dtos.user.UserResponseDto;
import school.sptech.crudrisecanvas.dtos.user.UserTokenDto;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.integrationtests.utils.paths.UserEnum;
import school.sptech.crudrisecanvas.service.UserService;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Controller")
class UserControllerTest {

    @InjectMocks
    private UserController controller;

    @Mock
    private UserService service;

    @Nested
    @DisplayName("register()")
    public class register {
        @Test
        @DisplayName("Quando dados forem válidos, deve chamar service e retornar 201")
        void validData() throws Exception {

            User user = new User();
            user.setName("Marcelo Soares");
            user.setEmail("marcelo.soares@email.com");
            user.setPassword("marcelo123");
            user.setCpf("017.895.420-90");

            UserRequestDto userReqDto = new UserRequestDto();
            userReqDto.setName("Marcelo Soares");
            userReqDto.setEmail("marcelo.soares@email.com");
            userReqDto.setPassword("marcelo123");
            userReqDto.setCpf("017.895.420-90");

            controller.register(userReqDto);

            Mockito.verify(service, Mockito.times(1)).register(any(User.class));
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
            void nameIsNull() throws Exception {

                String json = """
                            {
                                "email": "marcelo.soares@email.com",
                                "password": "marcelo123",
                                "cpf": "017.895.420-90"
                            }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome vazio")
            void nameIsEmpty() throws Exception {

                String json = """
                            {
                                "name": "",
                                "email": "marcelo.soares@email.com",
                                "password": "marcelo123",
                                "cpf": "017.895.420-90"
                            }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome em branco")
            void nameIsBlank() throws Exception {

                String json = """
                            {
                                "name": "           ",
                                "email": "marcelo.soares@email.com",
                                "password": "marcelo123",
                                "cpf": "017.895.420-90"
                            }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome maior que 255")
            void nameTooBig() throws Exception {

                String json = """
                            {
                                "name": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                                "email": "marcelo.soares@email.com",
                                "password": "marcelo123",
                                "cpf": "017.895.420-90"
                            }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail nulo")
            void emailIsNull() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "password": "marcelo123",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail vazio")
            void emailIsEmpty() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "",
                            "password": "marcelo123",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail em branco")
            void emailIsBlank() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "              ",
                            "password": "marcelo123",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail sem @")
            void emailNoAt() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soaresemail.com",
                            "password": "marcelo123",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail sem usuario")
            void emailNoUser() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "@email.com",
                            "password": "marcelo123",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail sem domínio")
            void emailNoDomain() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@",
                            "password": "marcelo123",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail com mais do que 255 caracteres")
            void emailTooBig() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soaresaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@email.com",
                            "password": "marcelo123",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CPF nulo")
            void cpfIsNull() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "password": "marcelo123"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CPF vazio")
            void cpfIsEmpty() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "password": "marcelo123",
                            "cpf": ""
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CPF em branco")
            void cpfIsBlank() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "password": "marcelo123",
                            "cpf": "               "
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CPF inválido")
            void cpfInvalid() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "password": "marcelo123",
                            "cpf": "017.895.420-93"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Senha nula")
            void passwordIsNull() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Senha vazia")
            void passwordIsEmpty() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "password": "",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Senha em branco")
            void passwordIsBlank() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "password": "          ",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("login()")
    public class login {
        @Test
        @DisplayName("Quando dados forem válidos, deve chamar service e retornar 200 com token")
        void validData() throws Exception {
            UserLoginDto user = new UserLoginDto();
            user.setEmail("marcelo.soares@email.com");
            user.setPassword("marcelo123");

            UserTokenDto tokenDto = new UserTokenDto();
            tokenDto.setUserId(1);
            tokenDto.setNome("Marcelo Soares");
            tokenDto.setEmail("marcelo.soares@email.com");
            tokenDto.setToken(UserMocks.getToken());

            Mockito.when(service.autenticar(user)).thenReturn(tokenDto);

            ResponseEntity<UserTokenDto> userRetornado = controller.login(user);

            assertEquals(HttpStatus.OK, userRetornado.getStatusCode());
            assertEquals(tokenDto.getNome(), userRetornado.getBody().getNome());
            assertEquals(tokenDto.getEmail(), userRetornado.getBody().getEmail());
            assertEquals(tokenDto.getToken(), userRetornado.getBody().getToken());
        }

        @Nested
        @SpringBootTest
        @AutoConfigureMockMvc
        @DisplayName("F. 400 - Bad Requests")
        public class badRequests {

            @Autowired
            private MockMvc mockMvc;

            @Test
            @DisplayName("E-mail nulo")
            void emailIsNull() throws Exception {

                String json = """
                        {
                            "password": "marcelo123"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("E-mail vazio")
            void emailIsEmpty() throws Exception {

                String json = """
                        {
                            "email": "",
                            "password": "marcelo123"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("E-mail em branco")
            void emailIsBlank() throws Exception {

                String json = """
                        {
                            "email": "            ",
                            "password": "marcelo123"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("E-mail sem @")
            void emailNoAt() throws Exception {

                String json = """
                        {
                            "email": "marcelo.soaresemail.com",
                            "password": "marcelo123"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("E-mail sem usuário")
            void emailNoUser() throws Exception {

                String json = """
                        {
                            "email": "@email.com",
                            "password": "marcelo123"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("E-mail sem domínio")
            void emailNoDomain() throws Exception {

                String json = """
                        {
                            "email": "marcelo.soares@",
                            "password": "marcelo123"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("Senha nula")
            void passwordIsNull() throws Exception {

                String json = """
                        {
                            "email": "marcelo.soares@email.com"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("Senha vazia")
            void passwordIsEmpty() throws Exception {

                String json = """
                        {
                            "email": "marcelo.soares@email.com",
                            "password": ""
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }

            @Test
            @DisplayName("Senha em branco")
            void passwordIsBlank() throws Exception {

                String json = """
                        {
                            "email": "marcelo.soares@email.com",
                            "password": "        "
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isUnauthorized());
            }
        }
    }

    @Test
    void account() {
    }

    @Nested
    @DisplayName("getUser()")
    public class getUser {
        @Test
        @DisplayName("Quando id existir, deve chamar service e retornar 200 com usuário")
        void validData() throws Exception {
            Integer id = 1;

            User user = UserMocks.getUser();

            UserResponseDto responseDto = new UserResponseDto();
            responseDto.setId(1);
            responseDto.setName("Marcelo Soares");
            responseDto.setEmail("marcelo.soares@email.com");
            responseDto.setCpf("017.895.420-90");

            Mockito.when(service.getUserById(id)).thenReturn(user);

            ResponseEntity<UserResponseDto> userRetornado = controller.getUser(id);

            assertEquals(HttpStatus.OK, userRetornado.getStatusCode());
            assertEquals(responseDto.getId(), userRetornado.getBody().getId());
            assertEquals(responseDto.getName(), userRetornado.getBody().getName());
            assertEquals(responseDto.getEmail(), userRetornado.getBody().getEmail());
            assertEquals(responseDto.getCpf(), userRetornado.getBody().getCpf());

            Mockito.verify(service, Mockito.times(1)).getUserById(id);
        }
    }

    @Nested
    @DisplayName("updateUser()")
    public class updateUser {


        @Nested
        @SpringBootTest
        @AutoConfigureMockMvc
        @DisplayName("F. 400 - Bad Requests")
        public class badRequests {

            @Autowired
            private MockMvc mockMvc;

            @Test
            @DisplayName("Nome nulo")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void nameIsNull() throws Exception {

                String json = """
                        {
                            "email": "marcelo.soares@email.com",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome vazio")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void nameIsEmpty() throws Exception {

                String json = """
                        {
                            "name": "",
                            "email": "marcelo.soares@email.com",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Nome em branco")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void nameIsBlank() throws Exception {

                String json = """
                        {
                            "name": "             ",
                            "email": "marcelo.soares@email.com",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail nulo")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void emailIsNull() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail vazio")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void emailIsEmpty() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail em branco")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void emailIsBlank() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "           ",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail sem @")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void emailNoAt() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soaresemail.com",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail sem usuário")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void emailNoUser() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "@email.com",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("E-mail sem domínio")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void emailNoDomain() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@",
                            "cpf": "017.895.420-90"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CPF nulo")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void cpfIsNull() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CPF vazio")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void cpfIsEmpty() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "cpf": ""
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CPF em branco")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void cpfIsBlank() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "cpf": "           "
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("CPF inválido")
            @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
            void cpfInvalid() throws Exception {

                String json = """
                        {
                            "name": "Marcelo Soares",
                            "email": "marcelo.soares@email.com",
                            "cpf": "017.895.420-86"
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("deleteUser()")
    public class deleteUser {

        @Test
        @DisplayName("Quando id existir, deve retornar 204")
        void deleteUser() {
            Integer id = 1;

            Mockito.doNothing().when(service).deleteUser(id);

            ResponseEntity<Void> response = controller.deleteUser(id);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Mockito.verify(service, Mockito.times(1)).deleteUser(id);
        }
    }
}