package school.sptech.crudrisecanvas.dtos.ong;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

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

    @NotBlank
    private String passwordUser;

    @NotBlank 
    @CPF
    private String cpfUser;

    @NotBlank
    private String emailUser;

    @NotBlank
    private String nameUser;
    
}
