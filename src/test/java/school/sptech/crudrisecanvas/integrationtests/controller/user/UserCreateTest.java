package school.sptech.crudrisecanvas.integrationtests.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import school.sptech.crudrisecanvas.integrationtests.utils.paths.UserEnum;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/data/truncate_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("Integration Test - User Creation")
public class UserCreateTest {

    @Nested
    @DisplayName("1. Valid scenarios")
    public class ValidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("1.1 Correct User Data (201)")
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
        }
    }

    @Nested
    @DisplayName("2. Invalid scenarios")
    public class InvalidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("2.1 Invalid CPF (400)")
        public void test1() throws Exception {

            String json = """
                    {
                        "name": "Marcelo Soares",
                        "email": "marcelo.soares@email.com",
                        "password": "marcelo123",
                        "cpf": "017.895.420-92"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("2.2 Duplicate CPF (409)")
        public void test2() throws Exception {

            String json1 = """
                    {
                        "name": "Marcelo Soares",
                        "email": "marcelo.soares@email.com",
                        "password": "marcelo123",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "name": "João Silva",
                        "email": "joao.silva@email.com",
                        "password": "joao@99",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("2.1 Invalid e-mail (400)")
        public void test3() throws Exception {

            String json = """
                    {
                        "name": "Marcelo Soares",
                        "email": "marcelo.soaresemail.com",
                        "password": "marcelo123",
                        "cpf": "017.895.420-92"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("2.4 Duplicate e-mail (409)")
        public void test4() throws Exception {

            String json1 = """
                    {
                        "name": "Marcelo Soares",
                        "email": "marcelo.soares@email.com",
                        "password": "marcelo123",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "name": "João Silva",
                        "email": "marcelo.soares@email.com",
                        "password": "joao@99",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        }
    }
}
