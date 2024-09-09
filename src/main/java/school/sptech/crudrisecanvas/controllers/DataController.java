package school.sptech.crudrisecanvas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Dados", description = "Endpoints para gerenciamento de dados de mapeamento")
public class DataController {

    private final MappingService mappingService;
    private final UserMappingService userMappingService;

    @GetMapping("/mapping/alerts")
    @Operation(
            summary = "Obter alertas de mapeamento",
            description = "Retorna uma lista de alertas de mapeamento antes da data fornecida.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de alertas de mapeamento retornada com sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum alerta de mapeamento encontrado")
            }
    )
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
    @Operation(
            summary = "Obter pontos de heatmap",
            description = "Retorna pontos de heatmap baseados no raio de agrupamento e data fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pontos de heatmap retornados com sucesso"),
                    @ApiResponse(responseCode = "204", description = "Nenhum ponto de heatmap encontrado")
            }
    )
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
    @Operation(
            summary = "Obter KPIs",
            description = "Retorna os KPIs totais ou KPIs após a data fornecida.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "KPIs retornados com sucesso")
            }
    )
    public ResponseEntity<MappingKpiDto> getKpis(@RequestParam(required = false) LocalDate afterDate) {
        if (afterDate == null) {
            return ResponseEntity.ok(mappingService.getKpisTotal());

        } else {
            return ResponseEntity.ok(mappingService.getKpisAfterDate(afterDate));
        }
    }

    @GetMapping("/mapping-count")
    @Operation(
            summary = "Obter contagem de mapeamentos por usuário",
            description = "Retorna a contagem de mapeamentos agrupados por usuário.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contagem de mapeamentos retornada com sucesso")
            }
    )
    public ResponseEntity<UserMappingCountResponseDto> getMappingCountByUser() {
        return ResponseEntity.status(200).body(userMappingService.getMappingCountByUser());
    }

    @GetMapping("/mapping/graph")
    @Operation(
            summary = "Obter gráfico de mapeamento",
            description = "Retorna dados para o gráfico de mapeamento para a data fornecida.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do gráfico de mapeamento retornados com sucesso")
            }
    )
    public ResponseEntity<List<MappingGraphDto>> getMappingGraph(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(mappingService.getMappingGraph(date));
    }
}
