package school.sptech.crudrisecanvas.dtos.ong;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;
import school.sptech.crudrisecanvas.dtos.address.AddressRequestUpdateDto;

@Data
public class OngRequestUpdateDto {
    @NotBlank
    private String name;

    @NotBlank
    @CNPJ
    private String cnpj;

    @NotNull
    @Valid
    private AddressRequestUpdateDto address;
}
