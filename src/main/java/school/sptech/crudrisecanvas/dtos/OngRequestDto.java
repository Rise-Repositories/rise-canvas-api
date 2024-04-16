package school.sptech.crudrisecanvas.dtos;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OngRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    @NotBlank
    @CNPJ
    private String cnpj;

    private String description;

    @NotBlank
    @Size(min = 8, max = 8)
    private String cep;
    
    @NotBlank
    private String address;
}
