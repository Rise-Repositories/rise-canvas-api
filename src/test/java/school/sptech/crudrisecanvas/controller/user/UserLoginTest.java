package school.sptech.crudrisecanvas.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import school.sptech.crudrisecanvas.utils.paths.UserEnum;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/data/truncate_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("User Login")
public class UserLoginTest {

    @Nested
    @DisplayName("1. Valid scenarios")
    public class ValidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("1.1 Correct login data (200)")
        public void test1() throws Exception {

            String json = """
                    {
                        "name": "Marcelo Soares",
                        "email": "marcelo.soares@email.com",
                        "password": "marcelo123",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "email": "marcelo.soares@email.com",
                        "password": "marcelo123"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").isNumber())
                    .andExpect(jsonPath("$.nome").value("Marcelo Soares"))
                    .andExpect(jsonPath("$.email").value("marcelo.soares@email.com"))
                    .andExpect(jsonPath("$.token").isNotEmpty())
                    .andExpect(jsonPath("$.password").doesNotExist());
        }
    }

    @Nested
    @DisplayName("2. Invalid scenarios")
    public class InvalidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("2.1 Incorrect email (401)")
        public void test1() throws Exception {

            String json = """
                    {
                        "name": "Marcelo Soares",
                        "email": "marcelo.soares@email.com",
                        "password": "marcelo123",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "email": "marcelo.silva@email.com",
                        "password": "marcelo123"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("2.2 Incorrect password (401)")
        public void test2() throws Exception {

            String json = """
                    {
                        "name": "Marcelo Soares",
                        "email": "marcelo.soares@email.com",
                        "password": "marcelo123",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "email": "marcelo.soares@email.com",
                        "password": "senha123"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.LOGIN.path)
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }
}
