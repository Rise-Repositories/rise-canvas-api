package school.sptech.crudrisecanvas.dtos.ong;

import jakarta.validation.Valid;
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

    @NotNull
    @Valid
    private AddressRequestUpdateDto address;
}
