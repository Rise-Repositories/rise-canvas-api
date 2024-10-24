package school.sptech.crudrisecanvas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import school.sptech.crudrisecanvas.dtos.address.AddressResponseDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingGraphDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseNoMappingRelationDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.MappingAction;
import school.sptech.crudrisecanvas.exception.BadRequestException;
import school.sptech.crudrisecanvas.repositories.MappingRepository;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {
    private final MappingRepository mappingRepository;
    private final MappingService mappingService;

    public byte[] getMappingArchiveTxt(LocalDate startDate, LocalDate endDate) {
        List<MappingResponseDto> mappings = MappingMapper.toResponse(mappingService.getMappingsByDate(startDate, endDate));
        int registroDeDados = 0;

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {

            String header = "00";
            header += String.format("%-11.11s", "MAPEAMENTO");
            header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            header += "01";
            writer.write(header);
            writer.newLine();

            for (MappingResponseDto m : mappings) {
                String corpo = "02";
                corpo += String.format("%-10.10s", m.getDate().toString());
                corpo += String.format("%-255.255s", m.getDescription());
                corpo += String.format("%-5.5s", m.getHasDisorders() ? "SIM" : "NAO");
                corpo += String.format("%-19.19s", m.getLatitude());
                corpo += String.format("%-19.19s", m.getLongitude());
                corpo += String.format("%03d", m.getQtyAdults());
                corpo += String.format("%03d", m.getQtyChildren());
                corpo += String.format("%-255.255s", m.getReferencePoint());
                corpo += String.format("%-8.8s", m.getStatus().toString());
                writer.write(corpo);
                writer.newLine();
                registroDeDados++;

                AddressResponseDto address = m.getAddress();
                if (address != null) {
                    String endereco = "03";
                    endereco += String.format("%-8.8s", address.getCep());
                    endereco += String.format("%-255.255s", address.getState());
                    endereco += String.format("%-255.255s", address.getNeighbourhood());
                    endereco += String.format("%-255.255s", address.getStreet());
                    endereco += String.format("%-6.6s", address.getNumber());
                    endereco += String.format("%-255.255s", address.getComplement());
                    writer.write(endereco);
                    writer.newLine();
                }
            }

            String trailer = "01";
            trailer += String.format("%05d", registroDeDados);
            writer.write(trailer);
            writer.newLine();

            writer.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("Erro ao gerar o arquivo: " + e.getMessage());
        }

        return null;
    }


    public void exportMappingGraphDtoToCsv(List<MappingGraphDto> mappingGraphData, PrintWriter writer) {
        writer.println("No_Served;Served;No_People;Year;Month");

        for (MappingGraphDto data : mappingGraphData) {
            writer.printf("%d;%d;%d;%d;%d%n",
                    data.getNo_Served(),
                    data.getServed(),
                    data.getNo_People(),
                    data.getYear(),
                    data.getMonth());
        }
    }


    public void processMappingArchiveTxt(String fileContent, String authorizationToken) {
        BufferedReader reader = new BufferedReader(new StringReader(fileContent));
        String registro;
        int contaRegistros = 0;
        int qtdGravados;

        try {
            registro = reader.readLine();
            Mapping mapping = null;

            while (registro != null) {
                String tipoRegistro = registro.substring(0, 2);

                if (tipoRegistro.equals("02")) {
                    String data = registro.substring(2, 12).trim();
                    String descricao = registro.substring(12, 267).trim();
                    boolean hasDisturbios = registro.substring(267, 272).trim().equalsIgnoreCase("SIM");
                    Double latitude = Double.valueOf(registro.substring(271, 291).trim());
                    Double longitude = Double.valueOf(registro.substring(291, 310).trim());
                    Integer qtyAdults = Integer.parseInt(registro.substring(310, 313).trim());
                    Integer qtyChildren = Integer.parseInt(registro.substring(313, 316).trim());
                    String referencePoint = registro.substring(316, 571).trim();
                    String statusString = registro.substring(571, 579).trim().toUpperCase();
                    MappingStatus status = statusString.equals("ACTIVE") ? MappingStatus.ACTIVE : MappingStatus.INACTIVE;

                    mapping = new Mapping();
                    mapping.setQtyAdults(qtyAdults);
                    mapping.setQtyChildren(qtyChildren);
                    mapping.setReferencePoint(referencePoint);
                    mapping.setHasDisorders(hasDisturbios);
                    mapping.setDescription(descricao);
                    mapping.setLatitude(latitude);
                    mapping.setLongitude(longitude);
                    mapping.setStatus(status);
                    mapping.setDate(LocalDate.parse(data));

                    contaRegistros++;

                } else if (tipoRegistro.equals("03")) {
                    String cep = registro.substring(2, 10).trim();
                    String estado = registro.substring(10, 265).trim();
                    String bairro = registro.substring(265, 520).trim();
                    String rua = registro.substring(520, 775).trim();
                    String numero = registro.substring(775, 780).trim();
                    String complemento = registro.substring(780, 1035).trim();

                    Address address = new Address();
                    address.setCep(cep);
                    address.setState(estado);
                    address.setNeighbourhood(bairro);
                    address.setStreet(rua);
                    address.setNumber(Integer.valueOf(numero));
                    address.setComplement(complemento);

                    if (mapping != null) {
                        mapping.setAddress(address);
                        mappingService.createMappingWithoutCepValidation(mapping, authorizationToken);
                        mapping = null;
                    }

                } else if (tipoRegistro.equals("01")) {
                    qtdGravados = Integer.parseInt(registro.substring(2, 7).trim());
                    if (contaRegistros != qtdGravados) {
                        throw new BadRequestException("Quantidade de registros inconsistente");
                    }
                }
                if (!tipoRegistro.equals("01") && !tipoRegistro.equals("02") && !tipoRegistro.equals("03") && !tipoRegistro.equals("00")) {
                    throw new BadRequestException("Registro inválido");
                }

                registro = reader.readLine();
            }
        } catch (IOException erro) {
            System.out.println("Erro ao ler o registro: " + erro.getMessage());
        }
    }
}
