package school.sptech.crudrisecanvas.dtos.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import school.sptech.crudrisecanvas.utils.annotations.CEP;

@Getter
@Setter
public class AddressRequestUpdateDto {

    @NotNull
    @NotBlank
    @CEP
    private String cep;
    private Integer number;
    private String complement;
}
