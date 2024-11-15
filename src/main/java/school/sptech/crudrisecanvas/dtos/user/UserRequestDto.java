package school.sptech.crudrisecanvas.dtos.user;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import school.sptech.crudrisecanvas.dtos.address.AddressRequestDto;

@Data
public class UserRequestDto {
    @Size(max = 255)
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    @CPF
    private String cpf;

    private AddressRequestDto address;
}