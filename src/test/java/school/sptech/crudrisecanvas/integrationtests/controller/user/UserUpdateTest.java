package school.sptech.crudrisecanvas.integrationtests.controller.user;

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
import school.sptech.crudrisecanvas.integrationtests.utils.paths.UserEnum;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/data/truncate_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("Integration Test - User Updates")
public class UserUpdateTest {


    @Nested
    @DisplayName("1. Valid scenarios")
    public class ValidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("1.1 Correct User Data (200)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test1() throws Exception {

            String json1 = """
                    {
                        "name": "Marcelo Soares",
                        "email": "marcelo.soares@email.com",
                        "password": "marcelo123",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(UserEnum.CREATE.path)
                            .content(json1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("authorization", "Bearer " + UserMocks.getToken()))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "name": "Marcelo Silva",
                        "email": "marcelo.silva@email.com",
                        "cpf": "569.848.380-96"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + UserMocks.getToken()))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("2. Invalid scenarios")
    public class InvalidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("2.1 No Authorization (401)")
        public void test1() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("2.2 Invalid CPF (400)")
        @WithMockUser(username = "testUser", password = "pass123")
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
                        "name": "Marcelo Silva",
                        "email": "marcelo.silva@email.com",
                        "cpf": "569.848.380-98"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("2.3 Invalid e-mail (400)")
        @WithMockUser(username = "testUser", password = "pass123")
        public void test3() throws Exception {

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
                        "name": "Marcelo Silva",
                        "email": "marcelo.silvaemail.com",
                        "cpf": "569.848.380-98"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "1")
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("2.4 ID Doesn't exist (404)")
        @WithMockUser(username = "testUser", password = "pass123")
        public void test4() throws Exception {

            String json2 = """
                    {
                        "name": "Marcelo Silva",
                        "email": "marcelo.silva@email.com",
                        "cpf": "017.895.420-90"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.put(UserEnum.BY_ID.path + "10")
                    .content(json2)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", "Bearer " + UserMocks.getToken()))
                    .andExpect(status().isNotFound());
        }
    }
}
