package school.sptech.crudrisecanvas.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import school.sptech.crudrisecanvas.dtos.address.AddressRequestDto;

@Data
public class UserRequestPatchPasswordDto {

    @NotBlank
    @Size
    private String curPassword;

    @NotBlank
    @Size(min = 6)
    private String newPassword;

}