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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = "/data/truncate_table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("ONG Creation")
public class OngCreateTest {

    @Nested
    @DisplayName("1. Valid scenarios")
    public class ValidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("1.1 Correct ONG Data (201)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test1() throws Exception {

            String json = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "44.454.154/0001-29",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Carlos Sales",
                            "email": "carlos.sales@email.com",
                            "password": "senha123",
                            "cpf": "680.312.080-50"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
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
        @DisplayName("2.1 Invalid CNPJ (400)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test1() throws Exception {

            String json = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "44.454.321/0001-29",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Carlos Sales",
                            "email": "carlos.sales@email.com",
                            "password": "senha123",
                            "cpf": "680.312.080-50"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("2.2 Duplicate CNPJ (409)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test2() throws Exception {

            String json1 = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "44.454.154/0001-29",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Carlos Sales",
                            "email": "carlos.sales@email.com",
                            "password": "senha123",
                            "cpf": "680.312.080-50"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "name": "Instituto ACB",
                        "cnpj": "44.454.154/0001-29",
                        "description": "Doação de alimentos",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Marcelo Sales",
                            "email": "marcelo.sales@email.com",
                            "password": "senha123",
                            "cpf": "062.862.290-27"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("2.3 Invalid cep (400)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test3() throws Exception {

            String json = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "44.454.154/0001-29",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "11111111",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Carlos Sales",
                            "email": "carlos.sales@email.com",
                            "password": "senha123",
                            "cpf": "680.312.080-50"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("2.4 Invalid User Email (400)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test4() throws Exception {

            String json = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "44.454.154/0001-29",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Carlos Sales",
                            "email": "carlos.salesemail.com",
                            "password": "senha123",
                            "cpf": "680.312.080-50"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("2.5 Duplicate User e-mail (409)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test5() throws Exception {

            String json1 = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "44.454.154/0001-29",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Carlos Sales",
                            "email": "carlos.sales@email.com",
                            "password": "senha123",
                            "cpf": "680.312.080-50"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "97.248.627/0001-06",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Marcelo Sales",
                            "email": "carlos.sales@email.com",
                            "password": "senha123",
                            "cpf": "335.103.360-54"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("2.6 Invalid User CPF (400)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test6() throws Exception {

            String json = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "44.454.154/0001-29",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Carlos Sales",
                            "email": "carlos.sales@email.com",
                            "password": "senha123",
                            "cpf": "680.312.080-53"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("2.7 Duplicate User CPF (409)")
        @WithMockUser(username = "marcelo.soares@email.com", password = "marcelo123")
        public void test7() throws Exception {

            String json1 = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "44.454.154/0001-29",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Carlos Sales",
                            "email": "carlos.sales@email.com",
                            "password": "senha123",
                            "cpf": "680.312.080-50"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());

            String json2 = """
                    {
                        "name": "Instituto A Corrente do Bem",
                        "cnpj": "97.248.627/0001-06",
                        "description": "Doação de alimentos e apoio a pessoas hipossuficientes",
                        "cep": "04446060",
                            "address": "Av. Assaré, Jardim Sabará",
                            "user": {
                            "name": "Marcelo Sales",
                            "email": "marcelo.sales@email.com",
                            "password": "senha123",
                            "cpf": "680.312.080-50"
                    	}
                    }""";

            mockMvc.perform(MockMvcRequestBuilders.post(OngEnum.BASE_URI.path)
                            .content(json2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        }
    }
}
