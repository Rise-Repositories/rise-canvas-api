package school.sptech.crudrisecanvas.integrationtests.controller.ong;

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
import school.sptech.crudrisecanvas.integrationtests.utils.paths.OngEnum;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/data/truncate_table.sql", "/data/add_ong.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("Ong Updates")
public class OngUpdateTest {

    @Nested
    @DisplayName("1. Valid scenarios")
    public class ValidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("1.1 Correct ONG Data (changing all values) (200)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test1() throws Exception {

            String json = """
                    {
                        "name": "Instituto ACB",
                        "description": "Promover a integração social entre voluntários e crianças",
                        "cep": "03679010",
                        "cnpj": "78.349.263/0001-06",
                        "address": "Rua Hélvio de Oliveira Albuquerque, 74"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.name").value("Instituto ACB"))
                    .andExpect(jsonPath("$.cep").value("03679010"))
                    .andExpect(jsonPath("$.cnpj").value("78.349.263/0001-06"))
                    .andExpect(jsonPath("$.address").value("Rua Hélvio de Oliveira Albuquerque, 74"))
                    .andExpect(jsonPath("$.description").value("Promover a integração social entre voluntários e crianças"));
        }

        @Test
        @DisplayName("1.2 Correct ONG Data (keeping current CPNJ) (200)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test2() throws Exception {

            String json = """
                    {
                        "name": "Instituto ACB",
                        "description": "Promover a integração social entre voluntários e crianças",
                        "cep": "03679010",
                        "cnpj": "20.438.196/0001-08",
                        "address": "Rua Hélvio de Oliveira Albuquerque, 74"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.name").value("Instituto ACB"))
                    .andExpect(jsonPath("$.cep").value("03679010"))
                    .andExpect(jsonPath("$.cnpj").value("20.438.196/0001-08"))
                    .andExpect(jsonPath("$.address").value("Rua Hélvio de Oliveira Albuquerque, 74"))
                    .andExpect(jsonPath("$.description").value("Promover a integração social entre voluntários e crianças"));
        }
    }

    @Nested
    @DisplayName("2. Invalid scenarios")
    public class InvalidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("2.1 Invalid CNPJ (400)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test1() throws Exception {

            String json = """
                    {
                        "name": "Instituto ACB",
                        "description": "Promover a integração social entre voluntários e crianças",
                        "cep": "03679010",
                        "cnpj": "78.349.123/0001-06",
                        "address": "Rua Hélvio de Oliveira Albuquerque, 74"
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.put(OngEnum.BY_ID.path + "1")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
