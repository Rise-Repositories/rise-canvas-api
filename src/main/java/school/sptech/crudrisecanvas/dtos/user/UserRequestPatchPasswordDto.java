package school.sptech.crudrisecanvas.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestPatchPasswordDto {

    @NotBlank
    @Size
    private String curPassword;

    @NotBlank
    @Size(min = 6)
    private String newPassword;

}