package school.sptech.crudrisecanvas.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import school.sptech.crudrisecanvas.dtos.address.AddressRequestUpdateDto;

@Data
public class UserRequestPatchDto {
    @Size(max = 255)
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @CPF
    private String cpf;

    private AddressRequestUpdateDto address;
}