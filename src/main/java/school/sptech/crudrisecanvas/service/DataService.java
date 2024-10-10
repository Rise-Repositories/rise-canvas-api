package school.sptech.crudrisecanvas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.sptech.crudrisecanvas.dtos.address.AddressResponseDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;
import school.sptech.crudrisecanvas.dtos.mappingAction.MappingActionResponseNoMappingRelationDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.entities.MappingAction;
import school.sptech.crudrisecanvas.repositories.MappingRepository;

import java.io.*;
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

                AddressResponseDto address = m.getAddress();
                corpo += String.format("%-8.8s", address != null ? address.getCep() : "");
                corpo += String.format("%-6.6s", address != null ? address.getNumber() : "");
                corpo += String.format("%-255.255s", address != null ? address.getComplement() : "");
                corpo += String.format("%-10.10s", m.getDate().toString());
                corpo += String.format("%-255.255s", m.getDescription());
                corpo += String.format("%-5.5s", m.getHasDisorders() ? "SIM" : "NAO");
                corpo += String.format("%-19.19s", m.getLatitude());
                corpo += String.format("%-19.19s", m.getLongitude());
                corpo += String.format("%03d", m.getQtyAdults());
                corpo += String.format("%03d", m.getQtyChildren());
                corpo += String.format("%-255.255s", m.getReferencePoint());
                corpo += String.format("%-8.8s", m.getStatus().toString());

                registroDeDados++;
                writer.write(corpo);
                writer.newLine();
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
}
