package school.sptech.crudrisecanvas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.io.PositionOutputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import school.sptech.crudrisecanvas.dtos.address.AddressResponseDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingGraphDto;
import school.sptech.crudrisecanvas.dtos.mapping.MappingMapper;
import school.sptech.crudrisecanvas.dtos.mapping.MappingResponseDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.Mapping;
import school.sptech.crudrisecanvas.exception.BadRequestException;
import school.sptech.crudrisecanvas.utils.Enums.MappingStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataService {
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

    public void exportMappingGraphDtoToJson(List<MappingGraphDto> mappingGraphData, PrintWriter writer) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            objectMapper.writeValue(writer, mappingGraphData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void exportMappingGraphDtoToXml(List<MappingGraphDto> mappingGraphData, PrintWriter writer) {
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<mappingGraphData>");

        for (MappingGraphDto data : mappingGraphData) {
            writer.println("  <mappingGraph>");
            writer.println("    <noServed>" + data.getNo_Served() + "</noServed>");
            writer.println("    <served>" + data.getServed() + "</served>");
            writer.println("    <noPeople>" + data.getNo_People() + "</noPeople>");
            writer.println("    <year>" + data.getYear() + "</year>");
            writer.println("    <month>" + data.getMonth() + "</month>");
            writer.println("  </mappingGraph>");
        }

        writer.println("</mappingGraphData>");
    }
    public InputStreamResource exportMappingGraphDtoToParquet(List<MappingGraphDto> mappingGraphData) throws IOException {
        String schemaString = "{\"type\":\"record\",\"name\":\"DataRecord\",\"fields\":["
            + "{\"name\":\"noServed\",\"type\":\"string\"},"
            + "{\"name\":\"served\",\"type\":\"string\"},"
            + "{\"name\":\"noPeople\",\"type\":\"int\"},"
            + "{\"name\":\"year\",\"type\":\"int\"},"
            + "{\"name\":\"month\",\"type\":\"int\"}"
            + "]}";
        Schema schema = new Schema.Parser().parse(schemaString);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ParquetWriter<GenericRecord> writer = AvroParquetWriter.<GenericRecord>builder(new org.apache.parquet.io.OutputFile() {
            @Override
            public PositionOutputStream create(long blockSizeHint) throws IOException {
                return new PositionOutputStream() {
                    @Override
                    public long getPos() throws IOException {
                        return outputStream.size();
                    }

                    @Override
                    public void write(int b) throws IOException {
                        outputStream.write(b);
                    }
                };
            }

            @Override
            public PositionOutputStream createOrOverwrite(long blockSizeHint) throws IOException {
                return create(blockSizeHint);
            }

            @Override
            public boolean supportsBlockSize() {
                return false;
            }

            @Override
            public long defaultBlockSize() {
                return 0;
            }
        })
                .withSchema(schema)
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .build()) {
            
            for (MappingGraphDto e : mappingGraphData) {
                GenericRecord record = new GenericData.Record(schema);
                record.put("noServed", e.getNo_Served());
                record.put("served", e.getServed());
                record.put("noPeople", e.getNo_People());
                record.put("year", e.getYear());
                record.put("month", e.getMonth());
                writer.write(record);
            }
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        InputStreamResource resource = new InputStreamResource(inputStream);

        return resource;
    }
}
