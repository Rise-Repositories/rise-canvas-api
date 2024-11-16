package school.sptech.crudrisecanvas.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import school.sptech.crudrisecanvas.dtos.mapping.MappingAlertDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingAlertResponseDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingKpiDto;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountResponseDto;
import school.sptech.crudrisecanvas.entities.Tags;
import school.sptech.crudrisecanvas.service.MappingService;
import school.sptech.crudrisecanvas.service.UserMappingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Data Controller")
class DataControllerTest {

    @InjectMocks
    private DataController controller;
    @Mock
    private MappingService mappingService;
    @Mock
    private UserMappingService userMappingService;

    @Nested
    @DisplayName("getMappingAlerts()")
    public class getMappingAlerts {

        @Test
        @DisplayName("V. Caso não haja alertas, deve retornar 204")
        void noData() {
            List<MappingAlertResponseDto> lista = Collections.emptyList();

            Mockito.when(mappingService.getMappingAlerts(LocalDate.now())).thenReturn(lista);

            ResponseEntity<List<MappingAlertResponseDto>> response = controller.getMappingAlerts(null);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            Mockito.verify(mappingService, Mockito.times(1)).getMappingAlerts(LocalDate.now());
        }

        @Test
        @DisplayName("V. Caso haja alertas, deve retornar 200")
        void tableHasData() {
            LocalDate agora = LocalDate.now();
            LocalDateTime agoraTime = LocalDateTime.now();
            MappingAlertResponseDto mapAlert = new MappingAlertResponseDto();

            Tags tag = new Tags();
            tag.setId(1);
            tag.setName("Comida");

            mapAlert.setMappingId(1);
            mapAlert.setDate(LocalDate.now());
            mapAlert.setLastServed(LocalDateTime.now());
            mapAlert.setAddress("Rua teste, 123");
            mapAlert.setTags(List.of(tag));

            List<MappingAlertResponseDto> lista = List.of(mapAlert);

            Mockito.when(mappingService.getMappingAlerts(LocalDate.now())).thenReturn(lista);

            ResponseEntity<List<MappingAlertResponseDto>> response = controller.getMappingAlerts(null);
            List<MappingAlertResponseDto> body = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(mapAlert.getMappingId(), body.get(0).getMappingId());
            assertEquals(mapAlert.getAddress(), body.get(0).getAddress());
            assertEquals(mapAlert.getDate(), body.get(0).getDate());
            assertEquals(mapAlert.getLastServed(), body.get(0).getLastServed());

            Mockito.verify(mappingService, Mockito.times(1)).getMappingAlerts(LocalDate.now());
        }
    }

    @Nested
    @DisplayName("getHeatmapPoints()")
    public class getHeatmapPoints {

        @Test
        @DisplayName("V. Caso não haja alertas, deve retornar 204")
        void noData() {
            Double[][] array = new Double[0][0];
            Double radius = 100.;
            LocalDateTime agora = LocalDateTime.now();

            Mockito.when(mappingService.getHeatmapPoints(radius, agora, null)).thenReturn(array);

            ResponseEntity<Double[][]> response = controller.getHeatmapPoints(radius, agora, null);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            Mockito.verify(mappingService, Mockito.times(1)).getHeatmapPoints(radius, agora, null);
        }

        @Test
        @DisplayName("V. Caso haja alertas, deve retornar 200")
        void tableHasData() {
            Double[][] array = { {1., 1.5}, {2., 2.5}};
            Double radius = 100.;
            LocalDateTime agora = LocalDateTime.now();

            Mockito.when(mappingService.getHeatmapPoints(radius, agora, null)).thenReturn(array);

            ResponseEntity<Double[][]> response = controller.getHeatmapPoints(radius, agora, null);

            Double[][] body = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(array[0][0], body[0][0]);
            assertEquals(array[0][1], body[0][1]);
            assertEquals(array[1][0], body[1][0]);
            assertEquals(array[1][1], body[1][1]);

            Mockito.verify(mappingService, Mockito.times(1)).getHeatmapPoints(radius, agora, null);
        }
    }

    @Nested
    @DisplayName("getKpis()")
    public class getKpis {

        @Test
        @DisplayName("V. Caso seja chamado sem data, deve chamar método certo e retornar 200")
        void noDate() {
            MappingKpiDto dto = new MappingKpiDto() {
                @Override
                public Integer getQtyTotal() {
                    return 50;
                }
                @Override
                public Integer getQtyServed() {
                    return 40;
                }
                @Override
                public Integer getQtyNotServed() {
                    return 8;
                }
                @Override
                public Integer getQtyNoPeople() {
                    return 2;
                }
            };

            LocalDate dataInicial = LocalDate.of(1000,1,1);
            LocalDate dataFinal = LocalDate.now().plusMonths(1);

            Mockito.when(mappingService.getKpisByDates(dataInicial, dataFinal, null)).thenReturn(dto);

            ResponseEntity<MappingKpiDto> response = controller.getKpis(null, null, null);
            MappingKpiDto body = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(dto.getQtyTotal(), body.getQtyTotal());
            assertEquals(dto.getQtyServed(), body.getQtyServed());
            assertEquals(dto.getQtyNotServed(), body.getQtyNotServed());
            assertEquals(dto.getQtyNoPeople(), body.getQtyNoPeople());

            Mockito.verify(mappingService, Mockito.times(1)).getKpisByDates(dataInicial, dataFinal, null);
        }

        @Test
        @DisplayName("V. Caso seja chamado comd data, deve chamar método certo e retornar 200")
        void withDate() {
            MappingKpiDto dto = new MappingKpiDto() {
                @Override
                public Integer getQtyTotal() {
                    return 50;
                }
                @Override
                public Integer getQtyServed() {
                    return 40;
                }
                @Override
                public Integer getQtyNotServed() {
                    return 8;
                }
                @Override
                public Integer getQtyNoPeople() {
                    return 2;
                }
            };

            LocalDate dataInicial = LocalDate.now().minusMonths(1);
            LocalDate dataFinal = LocalDate.now();

            Mockito.when(mappingService.getKpisByDates(dataInicial, dataFinal, null)).thenReturn(dto);

            ResponseEntity<MappingKpiDto> response = controller.getKpis(dataInicial, dataFinal, null);
            MappingKpiDto body = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(dto.getQtyTotal(), body.getQtyTotal());
            assertEquals(dto.getQtyServed(), body.getQtyServed());
            assertEquals(dto.getQtyNotServed(), body.getQtyNotServed());
            assertEquals(dto.getQtyNoPeople(), body.getQtyNoPeople());

            Mockito.verify(mappingService, Mockito.times(1)).getKpisByDates(dataInicial, dataFinal, null);
        }
    }

    @Test
    @DisplayName("getMappingCountByUser")
    void getMappingCountByUser() {
        UserMappingCountResponseDto dto = new UserMappingCountResponseDto();
        dto.addTo(1L);
        dto.addTo(2L);
        dto.addTo(0L);
        dto.addTo(3L);
        dto.addTo(4L);
        dto.addTo(5L);

        Mockito.when(userMappingService.getMappingCountByUser()).thenReturn(dto);

        ResponseEntity<UserMappingCountResponseDto> response = controller.getMappingCountByUser();
        UserMappingCountResponseDto body = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto.getZero(), body.getZero());
        assertEquals(dto.getOneOrTwo(), body.getOneOrTwo());
        assertEquals(dto.getThreeOrFour(), body.getThreeOrFour());
        assertEquals(dto.getFiveOrMore(), body.getFiveOrMore());

    }
}