package school.sptech.crudrisecanvas.dtos.ong;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;

@Data
public class OngRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    @CNPJ
    private String cnpj;

    private String description;

    @NotBlank
    @Size(min = 8, max = 8)
    private String cep;
    
    @NotBlank
    private String address;

    @NotNull
    private UserRequestDto user;
}