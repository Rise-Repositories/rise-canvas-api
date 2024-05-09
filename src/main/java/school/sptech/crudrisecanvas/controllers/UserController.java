package school.sptech.crudrisecanvas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
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
public class UserController {

    @Autowired
    UserRepositary userRepositary;
    @Autowired
    UserService usuarioService;

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

    /*
        TODO:
        criar um rota que o usuario consiga pegar os dados dele atraves do token
        vai descriptografar o token pegar o id dentro do token e procurar no banco
    */

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes do usuário"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado")
    })
    public ResponseEntity<UserResponseDto> getUser(@PathVariable int id) {
        User user = usuarioService.getUserById(id);

        UserResponseDto userDto = UserMapper.toDto(user);

        return ResponseEntity.status(200).body(userDto);
    }

    // @PostMapping
    // @Operation(summary = "Criar um novo usuário")
    // @ApiResponses(value = {
    //         @ApiResponse(responseCode = "201", description = "Criado - Retorna os detalhes do novo usuário"),
    //         @ApiResponse(responseCode = "409", description = "Conflito - O email ou CPF já está em uso")
    // })
    // public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userDto) {
    //     if(userRepositary.countWithEmailOrCpf(userDto.getEmail(), userDto.getCpf()) > 0) {
    //         return ResponseEntity.status(409).build();
    //     }
        
    //     User user = UserRequestMapper.toEntity(userDto);

    //     UserResponseDto result = UserResponseMapper.toDto(userRepositary.save(user));
    //     result.setMapping(new ArrayList<>());

    //     return ResponseEntity.status(201).body(result);
    // }



    // TODO: somente o proprio usuario pode alterar seus dados deve validar token

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes do usuário atualizado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito - O email ou CPF já está em uso")
    })
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable int id,@RequestBody @Valid UserRequestUpdateDto userDto) {
        User user = UserMapper.toEntity(userDto);

        UserResponseDto response = UserMapper.toDto(usuarioService.updateUser(id, user));

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