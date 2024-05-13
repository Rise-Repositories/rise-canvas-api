package school.sptech.crudrisecanvas.dtos.ong;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

@Data
public class OngRequestUpdateDto {
    @NotBlank
    private String name;

    @NotBlank
    @CNPJ
    private String cnpj;

    @NotBlank
    @Size(min = 8, max = 8)
    private String cep;
    
    @NotBlank
    private String address;
}
