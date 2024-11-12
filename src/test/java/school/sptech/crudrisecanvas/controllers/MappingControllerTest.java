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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import school.sptech.crudrisecanvas.dtos.mapping.MappingRequestDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.integrationtests.utils.paths.MappingEnum;
import school.sptech.crudrisecanvas.service.MappingService;
import school.sptech.crudrisecanvas.unittestutils.MappingMocks;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mapping Controller")
class MappingControllerTest {

    @InjectMocks
    private MappingController controller;

    @Mock
    private MappingService service;

    @Nested
    @DisplayName("getMappings()")
    public class getMappings {
        @Test
        @DisplayName("V. Quando tiver dados, deve chamar service e retornar lista")
        void tableHasData() {
            List<Mapping> mappings = MappingMocks.getMappingList();

            Mockito.when(service.getMappings()).thenReturn(mappings);

            ResponseEntity<List<MappingResponseDto>> response = controller.getMappings();
            List<MappingResponseDto> returnedMappings = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, returnedMappings.size());
            assertEquals(mappings.get(0).getId(), returnedMappings.get(0).getId());
            assertEquals(mappings.get(0).getQtyAdults(), returnedMappings.get(0).getQtyAdults());
            assertEquals(mappings.get(0).getQtyChildren(), returnedMappings.get(0).getQtyChildren());
            assertEquals(mappings.get(0).getReferencePoint(), returnedMappings.get(0).getReferencePoint());
            assertEquals(mappings.get(0).getHasDisorders(), returnedMappings.get(0).getHasDisorders());
            assertEquals(mappings.get(0).getDescription(), returnedMappings.get(0).getDescription());
            assertEquals(mappings.get(0).getLatitude(), returnedMappings.get(0).getLatitude());
            assertEquals(mappings.get(0).getLongitude(), returnedMappings.get(0).getLongitude());
            assertEquals(mappings.get(0).getStatus().toString(), returnedMappings.get(0).getStatus());
            assertEquals(mappings.get(0).getDate().toString(), returnedMappings.get(0).getDate());

            Mockito.verify(service, Mockito.times(1)).getMappings();
        }

        @Test
        @DisplayName("V. Quando não tiver dados, deve retornar 204")
        void tableNoData() {
            List<Mapping> mappings = Collections.emptyList();

            Mockito.when(service.getMappings()).thenReturn(mappings);

            ResponseEntity<List<MappingResponseDto>> response = controller.getMappings();

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Mockito.verify(service, Mockito.times(1)).getMappings();
        }
    }

    @Nested
    @DisplayName("getMappingById()")
    public class getMappingById {

        @Test
        @DisplayName("V. Quando o id existir, deve retornar o mapeamento")
        public void idExists() {
            Integer id = 1;
            Mapping mapping = MappingMocks.getMapping();

            Mockito.when(service.getMappingById(id)).thenReturn(mapping);

            ResponseEntity<MappingResponseDto> response = controller.getMappingById(id);
            MappingResponseDto returnedMapping = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(mapping.getId(), returnedMapping.getId());
            assertEquals(mapping.getQtyAdults(), returnedMapping.getQtyAdults());
            assertEquals(mapping.getQtyChildren(), returnedMapping.getQtyChildren());
            assertEquals(mapping.getReferencePoint(), returnedMapping.getReferencePoint());
            assertEquals(mapping.getHasDisorders(), returnedMapping.getHasDisorders());
            assertEquals(mapping.getDescription(), returnedMapping.getDescription());
            assertEquals(mapping.getLatitude(), returnedMapping.getLatitude());
            assertEquals(mapping.getLongitude(), returnedMapping.getLongitude());
            assertEquals(mapping.getStatus().toString(), returnedMapping.getStatus());
            assertEquals(mapping.getDate().toString(), returnedMapping.getDate());

            Mockito.verify(service, Mockito.times(1)).getMappingById(id);
        }
    }

    @Nested
    @DisplayName("createMapping()")
    public class createMapping {
        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 201")
        void validData() throws Exception {
            MappingRequestDto mapReqDto = MappingMocks.getMappingRequestDto();
            Mapping mapping = MappingMocks.getMapping();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authorization", UserMocks.getToken());

            Mockito.when(service.createMapping(any(), any(), any())).thenReturn(mapping);

            ResponseEntity<MappingResponseDto> response = controller.createMapping(mapReqDto, hashMap);
            MappingResponseDto returnedMapping = response.getBody();

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(mapping.getId(), returnedMapping.getId());
            assertEquals(mapping.getQtyAdults(), returnedMapping.getQtyAdults());
            assertEquals(mapping.getQtyChildren(), returnedMapping.getQtyChildren());
            assertEquals(mapping.getReferencePoint(), returnedMapping.getReferencePoint());
            assertEquals(mapping.getHasDisorders(), returnedMapping.getHasDisorders());
            assertEquals(mapping.getDescription(), returnedMapping.getDescription());
            assertEquals(mapping.getLatitude(), returnedMapping.getLatitude());
            assertEquals(mapping.getLongitude(), returnedMapping.getLongitude());
            assertEquals(mapping.getStatus().toString(), returnedMapping.getStatus());
            assertEquals(mapping.getDate().toString(), returnedMapping.getDate());

            Mockito.verify(service, Mockito.times(1)).createMapping(any(), any(), any());
        }

        @Nested
        @SpringBootTest
        @AutoConfigureMockMvc
        @DisplayName("F. 400 - Bad Requests")
        public class badRequests {

            @Autowired
            private MockMvc mockMvc;

            @Test
            @DisplayName("Qtde adultos nula")
            @WithMockUser(username = "testUser", password = "pass123")
            void qtyAdultsIsNull() throws Exception {
                String json = """
                        {
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": -23.123,
                            "longitude": -46.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Qtde adultos negativa")
            @WithMockUser(username = "testUser", password = "pass123")
            void qtyAdultsIsNegative() throws Exception {
                String json = """
                        {
                            "qtyAdults": -1,
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": -23.123,
                            "longitude": -46.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Qtde crianças nula")
            @WithMockUser(username = "testUser", password = "pass123")
            void qtyChildrenIsNull() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": -23.123,
                            "longitude": -46.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Qtde crianças negativa")
            @WithMockUser(username = "testUser", password = "pass123")
            void qtyChildrenIsNegative() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "qtyChildren": -1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": -23.123,
                            "longitude": -46.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Transtornos nulo")
            @WithMockUser(username = "testUser", password = "pass123")
            void hasDisordersIsNull() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "latitude": -23.123,
                            "longitude": -46.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Latitude nula")
            @WithMockUser(username = "testUser", password = "pass123")
            void latitudeIsNull() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "longitude": -46.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Latitude menor que -90")
            @WithMockUser(username = "testUser", password = "pass123")
            void latitudeLessThanMinus90() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": -90.1,
                            "longitude": -46.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Latitude maior que 90")
            @WithMockUser(username = "testUser", password = "pass123")
            void latitudeGreaterThan90() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": 90.1,
                            "longitude": -46.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Longitude nula")
            @WithMockUser(username = "testUser", password = "pass123")
            void longitudeIsNull() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": -23.123
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Longitude menor que -180")
            @WithMockUser(username = "testUser", password = "pass123")
            void longitudeLessThanMinus180() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": -23.123,
                            "longitude": -180.1
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("Longitude maior que 180")
            @WithMockUser(username = "testUser", password = "pass123")
            void longitudeGreaterThan180() throws Exception {
                String json = """
                        {
                            "qtyAdults": 1,
                            "qtyChildren": 1,
                            "referencePoint": "Muro Verde",
                            "hasDisorders": "false",
                            "latitude": -23.123,
                            "longitude": 180.1
                        }""";

                mockMvc.perform(MockMvcRequestBuilders.post(MappingEnum.BASE_URI.path)
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("authorization", "Bearer " + UserMocks.getToken()))
                        .andExpect(status().isBadRequest());
            }
        }
    }

    @Nested
    @DisplayName("updateMapping()")
    public class updateMapping {
        @Test
        @DisplayName("V. Quando dados forem válidos, deve chamar service e retornar 200")
        void validData() throws Exception {
            Integer id = 1;
            MappingRequestDto mapReqDto = MappingMocks.getMappingRequestDto();
            Mapping mapping = MappingMocks.getMapping();

            Mockito.when(service.updateMapping(any(), any())).thenReturn(mapping);

            ResponseEntity<MappingResponseDto> response = controller.updateMapping(id, mapReqDto);
            MappingResponseDto returnedMapping = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(mapping.getId(), returnedMapping.getId());
            assertEquals(mapping.getQtyAdults(), returnedMapping.getQtyAdults());
            assertEquals(mapping.getQtyChildren(), returnedMapping.getQtyChildren());
            assertEquals(mapping.getReferencePoint(), returnedMapping.getReferencePoint());
            assertEquals(mapping.getHasDisorders(), returnedMapping.getHasDisorders());
            assertEquals(mapping.getDescription(), returnedMapping.getDescription());
            assertEquals(mapping.getLatitude(), returnedMapping.getLatitude());
            assertEquals(mapping.getLongitude(), returnedMapping.getLongitude());
            assertEquals(mapping.getStatus().toString(), returnedMapping.getStatus());
            assertEquals(mapping.getDate().toString(), returnedMapping.getDate());

            Mockito.verify(service, Mockito.times(1)).updateMapping(any(), any());
        }

    }

    @Nested
    @DisplayName("deleteMapping()")
    public class deleteMapping {
        @Test
        @DisplayName("Quando id existir, deve retornar 204")
        void deleteMapping() {
            Integer id = 1;
            Mockito.doNothing().when(service).deleteMapping(id);

            ResponseEntity<Void> response = controller.deleteMapping(id);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            Mockito.verify(service, Mockito.times(1)).deleteMapping(id);
        }
    }

    @Nested
    @DisplayName("addUser()")
    public class addUser {

        @Test
        @DisplayName("Quando ids existirem, deve retornar 200")
        void addUser() {
            Integer mappingId = 1;
            Mapping mapping = MappingMocks.getMapping2();

            String token = UserMocks.getToken();
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("authorization", "Bearer " + UserMocks.getToken());

            Mockito.when(service.addUser(mappingId, token)).thenReturn(mapping);

            ResponseEntity<MappingResponseDto> response = controller.addUser(mappingId, hashMap);
            MappingResponseDto returnedMapping = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(mapping.getId(), returnedMapping.getId());
            assertEquals(mapping.getQtyAdults(), returnedMapping.getQtyAdults());
            assertEquals(mapping.getQtyChildren(), returnedMapping.getQtyChildren());
            assertEquals(mapping.getReferencePoint(), returnedMapping.getReferencePoint());
            assertEquals(mapping.getHasDisorders(), returnedMapping.getHasDisorders());
            assertEquals(mapping.getDescription(), returnedMapping.getDescription());
            assertEquals(mapping.getLatitude(), returnedMapping.getLatitude());
            assertEquals(mapping.getLongitude(), returnedMapping.getLongitude());
            assertEquals(mapping.getStatus().toString(), returnedMapping.getStatus());
            assertEquals(mapping.getDate().toString(), returnedMapping.getDate());
        }
    }
}