package school.sptech.crudrisecanvas.controllers;

import java.util.HashMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import school.sptech.crudrisecanvas.dtos.user.UserLoginDto;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;
import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.dtos.user.UserRequestUpdateDto;
import school.sptech.crudrisecanvas.dtos.user.UserResponseDto;
import school.sptech.crudrisecanvas.dtos.user.UserTokenDto;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.UserRepositary;
import school.sptech.crudrisecanvas.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
   private final UserRepositary userRepositary;
    private final UserService usuarioService;

    @PostMapping("/auth/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UserRequestDto userDto) {
        final User newUser = UserMapper.toEntity(userDto);

        this.usuarioService.register(newUser);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody UserLoginDto usuarioLoginDto) {
        UserTokenDto usuarioToken = this.usuarioService.autenticar(usuarioLoginDto);
        return ResponseEntity.status(200).body(usuarioToken);
    }

    @PatchMapping("/auth/recover")
    public ResponseEntity<String> recover(@RequestParam("email") String email) {
        this.usuarioService.recover(email);
        return ResponseEntity.status(204).build();
    }
    @PatchMapping("/auth/recover/{id}")
    public ResponseEntity<String> changePassword(@PathVariable int id, @RequestBody HashMap<String,String> body) {
        this.usuarioService.changePassword(id, body.get("password"));
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/account")
    public ResponseEntity<UserResponseDto> account(@RequestHeader HashMap<String,String> headers) {
        User user = usuarioService.getAccount(headers.get("authorization").substring(7));
        
        UserResponseDto response = UserMapper.toResponse(user);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes do usuário"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado")
    })
    public ResponseEntity<UserResponseDto> getUser(@PathVariable int id) {
        User user = usuarioService.getUserById(id);

        UserResponseDto userDto = UserMapper.toResponse(user);

        return ResponseEntity.status(200).body(userDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes do usuário atualizado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito - O email ou CPF já está em uso")
    })
    public ResponseEntity<UserResponseDto> updateUser(
        @PathVariable int id,
        @RequestBody @Valid UserRequestUpdateDto userDto,
        @RequestHeader HashMap<String,String> headers
    ) {
        User user = UserMapper.toEntity(userDto);

        UserResponseDto response = UserMapper.toResponse(usuarioService.updateUser(id, user, headers.get("authorization").substring(7)));

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userRepositary.deleteById(id);

        return ResponseEntity.status(204).build();
    }
}