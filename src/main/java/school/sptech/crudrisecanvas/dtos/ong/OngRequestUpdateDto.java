package school.sptech.crudrisecanvas.dtos.ong;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;
import school.sptech.crudrisecanvas.dtos.address.AddressRequestUpdateDto;
import school.sptech.crudrisecanvas.utils.annotations.CEP;

@Data
public class OngRequestUpdateDto {
    @NotBlank
    private String name;

    @NotBlank
    @CNPJ
    private String cnpj;

    @NotBlank
    @Size(min = 8, max = 9)
    @CEP
    private String cep;

    @NotNull
    private AddressRequestUpdateDto address;
}
