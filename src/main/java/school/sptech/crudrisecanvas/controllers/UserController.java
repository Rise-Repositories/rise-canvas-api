package school.sptech.crudrisecanvas.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import school.sptech.crudrisecanvas.dtos.UserRequestDto;
import school.sptech.crudrisecanvas.dtos.UserRequestMapper;
import school.sptech.crudrisecanvas.dtos.UserRequestUpdateDto;
import school.sptech.crudrisecanvas.dtos.UserResponseDto;
import school.sptech.crudrisecanvas.dtos.UserResponseMapper;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.repositories.UserRepositary;
import school.sptech.crudrisecanvas.service.usuario.UsuarioService;
import school.sptech.crudrisecanvas.service.usuario.autenticacao.dto.UsuarioLoginDto;
import school.sptech.crudrisecanvas.service.usuario.autenticacao.dto.UsuarioTokenDto;
import school.sptech.crudrisecanvas.service.usuario.dto.UsuarioCriacaoDto;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepositary userRepositary;
    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    //@SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> criar(@RequestBody @Valid UsuarioCriacaoDto novoUsuarioDto) {
        this.usuarioService.criar(novoUsuarioDto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        UsuarioTokenDto usuarioToken = this.usuarioService.autenticar(usuarioLoginDto);
        return ResponseEntity.status(200).body(usuarioToken);
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna a lista de usuários"),
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - Não há usuários cadastrados")
    })
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<User> users = userRepositary.findAll();
        if(users.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        List<UserResponseDto> usersDto = UserResponseMapper.toDto(users);
        return ResponseEntity.status(200).body(usersDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes do usuário"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado")
    })
    public ResponseEntity<UserResponseDto> getUser(@PathVariable int id) {
        Optional<User> user = userRepositary.findById(id);

        if(user.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        UserResponseDto userDto = UserResponseMapper.toDto(user.get());

        return ResponseEntity.status(200).body(userDto);
    }

    @PostMapping
    @Operation(summary = "Criar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado - Retorna os detalhes do novo usuário"),
            @ApiResponse(responseCode = "409", description = "Conflito - O email ou CPF já está em uso")
    })
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userDto) {
        if(userRepositary.countWithEmailOrCpf(userDto.getEmail(), userDto.getCpf()) > 0) {
            return ResponseEntity.status(409).build();
        }
        
        User user = UserRequestMapper.toEntity(userDto);

        UserResponseDto result = UserResponseMapper.toDto(userRepositary.save(user));
        result.setMapping(new ArrayList<>());

        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Retorna os detalhes do usuário atualizado"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito - O email ou CPF já está em uso")
    })
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable int id,@RequestBody @Valid UserRequestUpdateDto user) {
        Optional<User> userOptional = userRepositary.findById(id);

        if(userOptional.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        if(userRepositary.countWithEmailOrCpfAndDiferentId(user.getEmail(), user.getCpf(), id) > 0) {
            return ResponseEntity.status(409).build();
        }

        User userEntity = userOptional.get();

        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setCpf(user.getCpf());
        userEntity.setIp(user.getIp());

        UserResponseDto result = UserResponseMapper.toDto(userRepositary.save(userEntity));

        return ResponseEntity.status(200).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um usuário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sem conteúdo - Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não encontrado - Usuário não encontrado")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if(userRepositary.countWithId(id) == 0) {
            return ResponseEntity.status(404).build();
        }
        userRepositary.deleteById(id);
        return ResponseEntity.status(204).build();
    }
}