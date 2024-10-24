package school.sptech.crudrisecanvas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.sptech.crudrisecanvas.dtos.mapping.MappingAlertDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingGraphDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingKpiDto;
import school.sptech.crudrisecanvas.dtos.userMapping.UserMappingCountResponseDto;
import school.sptech.crudrisecanvas.service.DataService;
import school.sptech.crudrisecanvas.service.MappingService;
import school.sptech.crudrisecanvas.service.UserMappingService;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
@Tag(name = "Dados", description = "Endpoints para gerenciamento de dados de mapeamento")
public class DataController {

    private final MappingService mappingService;
    private final UserMappingService userMappingService;
    private final DataService dataService;

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
            description = "Retorna os KPIs totais ou KPIs entre as datas fornecidas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "KPIs retornados com sucesso")
            }
    )
    public ResponseEntity<MappingKpiDto> getKpis(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        if (startDate == null) {
            startDate = LocalDate.of(1000, 1,1);
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusMonths(1);
        }
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(mappingService.getKpisByDates(startDate, endDate));
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
            description = "Retorna dados para o gráfico de mapeamento entre as datas fornecidas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dados do gráfico de mapeamento retornados com sucesso")
            }
    )
    public ResponseEntity<List<MappingGraphDto>> getMappingGraph(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(mappingService.getMappingGraph(startDate, endDate));
    }

    @GetMapping(value = "/mapping/archive/txt", produces = "text/plain")
    @Operation(
            summary = "Obter arquivos de mapeamento em txt",
            description = "Retorna arquivos de mapeamento em txt entre as datas fornecidas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Arquivos de mapeamento em txt retornados com sucesso")
            }
    )
    public ResponseEntity<byte[]> getMappingArchiveTxt(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        if (startDate == null) {
            startDate = LocalDate.of(1000, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusMonths(1);
        }
        if (startDate.isAfter(endDate)) {
            return ResponseEntity.badRequest().build();
        }

        // Obtenha o conteúdo do arquivo em bytes
        byte[] arquivoTxt = dataService.getMappingArchiveTxt(startDate, endDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mappingArchive.txt")
                .body(arquivoTxt);
    }

    @GetMapping("/export-csv")
    @Operation(
            summary = "Exportar dados para CSV",
            description = "Exporta os dados entre as datas especificadas no formato CSV.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Arquivos de mapeamento em CSV retornados com sucesso"),
                    @ApiResponse(responseCode = "204", description = "Não há conteúdo para exportar"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    public ResponseEntity<Void> exportCsv(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response) {

        try {
            List<MappingGraphDto> dataList = mappingService.getMappingGraph(startDate, endDate);

            if (dataList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"mapping_graph.csv\"");

            dataService.exportMappingGraphDtoToCsv(dataList, response.getWriter());
          
             return ResponseEntity.ok().build();

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
    }
  
    @PostMapping(value = "/mapping/archive/txt", consumes = "text/plain")
    @Operation(
            summary = "Upload de arquivo de mapeamento em txt",
            description = "Recebe um arquivo de mapeamento em formato de texto.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Arquivo de mapeamento recebido com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Falha no upload do arquivo")
            }
    )
    public ResponseEntity<String> uploadMappingArchiveTxt(
            @RequestBody String fileContent,
            @RequestHeader HashMap<String, String> headers
    ) {
        if (fileContent.isEmpty()) {
            return ResponseEntity.badRequest().body("O arquivo está vazio.");
        }
        if (!headers.containsKey("authorization") || headers.get("authorization").length() < 8) {
            return ResponseEntity.badRequest().body("Cabeçalho de autorização inválido.");
        }

        try {
            String token = headers.get("authorization").substring(7);
            dataService.processMappingArchiveTxt(fileContent, token);
            return ResponseEntity.ok("Arquivo recebido e processado com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o arquivo: " + e.getMessage());
        }
    }




}
