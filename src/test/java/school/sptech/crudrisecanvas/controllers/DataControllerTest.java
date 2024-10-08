package school.sptech.crudrisecanvas.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import school.sptech.crudrisecanvas.dtos.mapping.MappingAlertDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingKpiDto;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountResponseDto;
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
            List<MappingAlertDto> lista = Collections.emptyList();

            Mockito.when(mappingService.getMappingAlerts(LocalDate.now())).thenReturn(lista);

            ResponseEntity<List<MappingAlertDto>> response = controller.getMappingAlerts(null);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            Mockito.verify(mappingService, Mockito.times(1)).getMappingAlerts(LocalDate.now());
        }

        @Test
        @DisplayName("V. Caso haja alertas, deve retornar 200")
        void tableHasData() {
            LocalDate agora = LocalDate.now();
            LocalDateTime agoraTime = LocalDateTime.now();
            MappingAlertDto mapAlert = new MappingAlertDto() {
                @Override
                public Integer getMappingId() {
                    return 1;
                }
                @Override
                public String getAddress() {
                    return "Rua 1";
                }
                @Override
                public LocalDate getDate() {
                    return agora;
                }
                @Override
                public LocalDateTime getLastServed() {
                    return agoraTime;
                }
            };
            List<MappingAlertDto> lista = List.of(mapAlert);

            Mockito.when(mappingService.getMappingAlerts(LocalDate.now())).thenReturn(lista);

            ResponseEntity<List<MappingAlertDto>> response = controller.getMappingAlerts(null);
            List<MappingAlertDto> body = response.getBody();

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

            Mockito.when(mappingService.getHeatmapPoints(radius, agora)).thenReturn(array);

            ResponseEntity<Double[][]> response = controller.getHeatmapPoints(radius, agora);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

            Mockito.verify(mappingService, Mockito.times(1)).getHeatmapPoints(radius, agora);
        }

        @Test
        @DisplayName("V. Caso haja alertas, deve retornar 200")
        void tableHasData() {
            Double[][] array = { {1., 1.5}, {2., 2.5}};
            Double radius = 100.;
            LocalDateTime agora = LocalDateTime.now();

            Mockito.when(mappingService.getHeatmapPoints(radius, agora)).thenReturn(array);

            ResponseEntity<Double[][]> response = controller.getHeatmapPoints(radius, agora);

            Double[][] body = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(array[0][0], body[0][0]);
            assertEquals(array[0][1], body[0][1]);
            assertEquals(array[1][0], body[1][0]);
            assertEquals(array[1][1], body[1][1]);

            Mockito.verify(mappingService, Mockito.times(1)).getHeatmapPoints(radius, agora);
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

            Mockito.when(mappingService.getKpisTotal()).thenReturn(dto);

            ResponseEntity<MappingKpiDto> response = controller.getKpis(null);
            MappingKpiDto body = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(dto.getQtyTotal(), body.getQtyTotal());
            assertEquals(dto.getQtyServed(), body.getQtyServed());
            assertEquals(dto.getQtyNotServed(), body.getQtyNotServed());
            assertEquals(dto.getQtyNoPeople(), body.getQtyNoPeople());

            Mockito.verify(mappingService, Mockito.times(1)).getKpisTotal();
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

            LocalDate agora = LocalDate.now();

            Mockito.when(mappingService.getKpisAfterDate(agora)).thenReturn(dto);

            ResponseEntity<MappingKpiDto> response = controller.getKpis(agora);
            MappingKpiDto body = response.getBody();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(dto.getQtyTotal(), body.getQtyTotal());
            assertEquals(dto.getQtyServed(), body.getQtyServed());
            assertEquals(dto.getQtyNotServed(), body.getQtyNotServed());
            assertEquals(dto.getQtyNoPeople(), body.getQtyNoPeople());

            Mockito.verify(mappingService, Mockito.times(1)).getKpisAfterDate(agora);
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