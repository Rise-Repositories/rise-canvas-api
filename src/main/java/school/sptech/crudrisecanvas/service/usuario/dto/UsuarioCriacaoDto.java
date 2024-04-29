package school.sptech.crudrisecanvas.service.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UsuarioCriacaoDto {

    @Size(min = 3, max = 20)
    @Schema(description = "Nome do usuário", example = "Rafael Reis")
    private String name;
    @Email
    @Schema(description = "Email do usuário", example = "rafael.reis@sptech.school")
    private String email;
    @Size(min = 6, max = 20)
    @Schema(description = "Senha do usuário", example = "AdminAdmin")
    private String password;

    private String cpf;
    private String ip;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
