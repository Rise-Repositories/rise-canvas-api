package school.sptech.crudrisecanvas.integrationtests.controller.ong;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@Sql(scripts = {"/data/truncate_table.sql", "/data/add_ong.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisplayName("Integration Test - ONG Delete")
public class OngDeleteTest {

    @Nested
    @DisplayName("1. Valid scenarios")
    public class ValidScenarios {

        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("1.1 Correct ID (204)")
        @WithMockUser(username = "testUser", password = "pass123")
        public void test1() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete(OngEnum.BY_ID.path + "1"))
                    .andExpect(status().isNoContent());
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

            mockMvc.perform(MockMvcRequestBuilders.delete(OngEnum.BY_ID.path + "1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("2.2 ID doesn't exists (404)")
        @WithMockUser(username = "testUser", password = "pass123")
        public void test2() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.delete(OngEnum.BY_ID.path + "10"))
                    .andExpect(status().isNotFound());
        }
    }
}
