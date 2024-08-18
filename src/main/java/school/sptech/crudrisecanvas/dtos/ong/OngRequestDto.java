package school.sptech.crudrisecanvas.dtos.ong;

import jakarta.validation.Valid;
import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import school.sptech.crudrisecanvas.dtos.address.AddressRequestDto;
import school.sptech.crudrisecanvas.utils.annotations.CEP;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;

@Data
public class OngRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    @CNPJ
    private String cnpj;
    
    @NotNull
    @Valid
    private AddressRequestDto address;

    @NotNull
    @Valid
    private UserRequestDto user;
}
