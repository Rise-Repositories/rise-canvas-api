package school.sptech.crudrisecanvas.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.sptech.crudrisecanvas.dtos.mapping.MappingAlertDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingGraphDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingKpiDto;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountResponseDto;
import school.sptech.crudrisecanvas.service.MappingService;
import school.sptech.crudrisecanvas.service.UserMappingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {

    private final MappingService mappingService;
    private final UserMappingService userMappingService;

    @GetMapping("/mapping/alerts")
    public ResponseEntity<List<MappingAlertDto>> getMappingAlerts(@RequestParam(required = false) LocalDate beforeDate
    ) {
        if (beforeDate == null) {
            beforeDate = LocalDate.now();
        }
        List<MappingAlertDto> mappingAlerts = mappingService.getMappingAlerts(beforeDate);

        if (mappingAlerts.isEmpty()) {
            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.ok(mappingAlerts);
        }
    }

    @GetMapping("/heatmap")
    public ResponseEntity<Double[][]> getHeatmapPoints(
            @RequestParam double radiusToGroup,
            @RequestParam LocalDateTime olderThan
    ) {
        Double[][] heatmapPoints = mappingService.getHeatmapPoints(radiusToGroup, olderThan);
        if (heatmapPoints.length == 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(heatmapPoints);
        }
    }

    @GetMapping("/kpi")
    public ResponseEntity<MappingKpiDto> getKpis(@RequestParam(required = false) LocalDate afterDate) {
        if (afterDate == null) {
            return ResponseEntity.ok(mappingService.getKpisTotal());

        } else {
            return ResponseEntity.ok(mappingService.getKpisAfterDate(afterDate));
        }
    }

    @GetMapping("/mapping-count")
    public ResponseEntity<UserMappingCountResponseDto> getMappingCountByUser() {
        return ResponseEntity.status(200).body(userMappingService.getMappingCountByUser());
    }

    @GetMapping("/mapping/graph")
    public ResponseEntity<List<MappingGraphDto>> getMappingGraph(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(mappingService.getMappingGraph(date));
    }
}
