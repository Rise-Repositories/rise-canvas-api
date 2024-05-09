package school.sptech.crudrisecanvas.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank
    private String email;

    /*
        TODO:
        precisa ver as regras que iremos colocar par valiadacao
        minimia de senha tipo tem que te @#$% e etc
    */

    @NotBlank
    private String password;
}
