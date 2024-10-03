package school.sptech.crudrisecanvas.controllers;

import java.util.HashMap;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import school.sptech.crudrisecanvas.dtos.address.AddressMapper;
import school.sptech.crudrisecanvas.dtos.user.UserLoginDto;
import school.sptech.crudrisecanvas.dtos.user.UserRequestDto;
import school.sptech.crudrisecanvas.dtos.user.UserMapper;
import school.sptech.crudrisecanvas.dtos.user.UserRequestUpdateDto;
import school.sptech.crudrisecanvas.dtos.user.UserResponseDto;
import school.sptech.crudrisecanvas.dtos.user.UserTokenDto;
import school.sptech.crudrisecanvas.entities.Address;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {
    private final UserService usuarioService;

    @PostMapping("/auth/register")
    @Operation(
            summary = "Registrar um novo usuário",
            description = "Cria um novo usuário com os dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Criado - Usuário registrado com sucesso"),
                    @ApiResponse(responseCode = "409", description = "Conflito - Email ou CPF já está em uso")
            }
    )
    public ResponseEntity<Void> register(@RequestBody @Valid UserRequestDto userDto) {
        final User newUser = UserMapper.toEntity(userDto);
        this.usuarioService.register(newUser);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/auth/login")
    @Operation(
            summary = "Login do usuário",
            description = "Autentica um usuário e retorna um token de acesso.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Retorna o token de acesso do usuário"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado - Credenciais inválidas")
            }
    )
    public ResponseEntity<UserTokenDto> login(@RequestBody @Valid UserLoginDto usuarioLoginDto) {
        UserTokenDto usuarioToken = this.usuarioService.autenticar(usuarioLoginDto);
        return ResponseEntity.status(200).body(usuarioToken);
    }

    @PatchMapping("/auth/recover")
    @Operation(
            summary = "Recuperar senha do usuário",
            description = "Envia um e-mail para recuperação de senha para o endereço fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Sem conteúdo - E-mail enviado para recuperação de senha"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida - E-mail não encontrado")
            }
    )
    public ResponseEntity<String> recover(@RequestParam("email") String email) {
        this.usuarioService.recover(email);
        return ResponseEntity.status(204).build();
    }
    @PatchMapping("/auth/recover/{id}")
    @Operation(
            summary = "Alterar senha do usuário",
            description = "Altera a senha de um usuário identificado pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Sem conteúdo - Senha alterada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Solicitação inválida - Senha não fornecida")
            }
    )
    public ResponseEntity<String> changePassword(@PathVariable int id, @RequestBody HashMap<String,String> body) {
        this.usuarioService.changePassword(id, body.get("password"));
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/account")
    @Operation(
            summary = "Obter detalhes da conta do usuário",
            description = "Retorna os detalhes da conta do usuário autenticado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes da conta do usuário"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado - Token de acesso inválido")
            }
    )
    public ResponseEntity<UserResponseDto> account(@RequestHeader HashMap<String,String> headers) {
        System.out.println(headers.get("authorization"));
        User user = usuarioService.getAccount(headers.get("authorization").substring(7));
        
        UserResponseDto response = UserMapper.toResponse(user);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obter detalhes de um usuário pelo ID",
            description = "Retorna os detalhes de um usuário específico identificado pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes do usuário"),
                    @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado")
            }
    )
    public ResponseEntity<UserResponseDto> getUser(@PathVariable int id) {
        User user = usuarioService.getUserById(id);

        UserResponseDto userDto = UserMapper.toResponse(user);

        return ResponseEntity.status(200).body(userDto);
    }
    
    @GetMapping
    @Operation(
            summary = "Listar usuários por e-mail",
            description = "Retorna uma lista de usuários cujo e-mail corresponde ao fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK - Retorna a lista de usuários"),
                    @ApiResponse(responseCode = "204", description = "Sem conteúdo - Nenhum usuário encontrado com o e-mail fornecido")
            }
    )
    public ResponseEntity<List<UserResponseDto>> getUsers(@RequestParam("email") String email) {
        List<User> user = usuarioService.getUsersByEmail(email);

        List<UserResponseDto> userDto = UserMapper.toResponse(user);

        if(userDto.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

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

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar um usuário sem sobrescrever dados não passados ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes do usuário atualizado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito - O email ou CPF já está em uso")
    })
    public ResponseEntity<UserResponseDto> patchUser(
            @PathVariable int id,
            @RequestBody UserRequestUpdateDto userDto,
            @RequestHeader HashMap<String,String> headers
    ) {
        User user = UserMapper.toEntity(userDto);

        UserResponseDto response = UserMapper.toResponse(usuarioService.patchUser(id, user, headers.get("authorization").substring(7)));

        return ResponseEntity.status(200).body(response);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        usuarioService.deleteUser(id);

        return ResponseEntity.status(204).build();
    }

    @GetMapping("/total-count")
    public ResponseEntity<Long> getUserCount() {
        return ResponseEntity.status(200).body(usuarioService.getUserCount());
    }
}