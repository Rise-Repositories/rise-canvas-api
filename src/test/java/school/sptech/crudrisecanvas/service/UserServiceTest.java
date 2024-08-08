package school.sptech.crudrisecanvas.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import school.sptech.crudrisecanvas.api.configuration.security.jwt.JwtTokenManager;
import school.sptech.crudrisecanvas.dtos.user.UserLoginDto;
import school.sptech.crudrisecanvas.dtos.user.UserTokenDto;
import school.sptech.crudrisecanvas.entities.User;
import school.sptech.crudrisecanvas.exception.ConflictException;
import school.sptech.crudrisecanvas.exception.ForbiddenException;
import school.sptech.crudrisecanvas.exception.NotFoundException;
import school.sptech.crudrisecanvas.repositories.UserRepositary;
import school.sptech.crudrisecanvas.unittestutils.UserMocks;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service")
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepositary repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenManager gerenciadorTokenJwt;
    @Mock
    private AuthenticationManager authenticationManager;

    @Nested
    @DisplayName("register()")
    public class register {

        @Test
        @DisplayName("V. Quando dados forem válidos, deve salvar usuário")
        void validData() {
            User user = UserMocks.getUser();
            String passwordHash = "oighfcvhjklçgfhjkopiuytfguhij";
            String oldPassword = user.getPassword();

            Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(passwordHash);
            Mockito.when(repository.existsByCpf(user.getCpf())).thenReturn(false);
            Mockito.when(repository.existsByEmail(user.getEmail())).thenReturn(false);

            service.register(user);

            Mockito.verify(repository, Mockito.times(1)).save(user);
            Mockito.verify(repository, Mockito.times(1)).existsByCpf(user.getCpf());
            Mockito.verify(repository, Mockito.times(1)).existsByEmail(user.getEmail());
            Mockito.verify(passwordEncoder, Mockito.times(1)).encode(oldPassword);
        }

        @Test
        @DisplayName("F. Quando CPF já existir, deve lançar ConflictException")
        void repeatCPF() {
            User user = UserMocks.getUser();

            Mockito.when(repository.existsByCpf(user.getCpf())).thenReturn(true);

            ConflictException exception = assertThrows(
                    ConflictException.class,
                    () -> service.register(user)
            );

            assertEquals("CPF já cadastrado", exception.getLocalizedMessage());

            Mockito.verify(repository, Mockito.times(1)).existsByCpf(user.getCpf());
        }

        @Test
        @DisplayName("F. Quando e-mail já existir, deve lançar ConflictException")
        void repeatEmail() {
            User user = UserMocks.getUser();

            Mockito.when(repository.existsByCpf(user.getCpf())).thenReturn(false);
            Mockito.when(repository.existsByEmail(user.getEmail())).thenReturn(true);

            ConflictException exception = assertThrows(
                    ConflictException.class,
                    () -> service.register(user)
            );

            assertEquals("E-mail já cadastrado", exception.getLocalizedMessage());

            Mockito.verify(repository, Mockito.times(1)).existsByEmail(user.getEmail());
        }
    }

    @Nested
    @DisplayName("autenticar()")
    public class autenticar {

        @Test
        @DisplayName("V. Quando email e senha estão corretos, deve retornar UserTokenDto")
        void validData() {
            UserLoginDto loginDto = new UserLoginDto();
            loginDto.setEmail("usuario@email.com");
            loginDto.setPassword("senha123");

            UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                    "usuario@email.com", "senha123"
            );

            User user = UserMocks.getUser();

            final Authentication authentication = authenticationManager.authenticate(credentials);

            User userAutenticado = UserMocks.getAuthUser();

            String token = UserMocks.getToken();

            UserTokenDto tokenDto = new UserTokenDto();
            tokenDto.setUserId(1);
            tokenDto.setNome("usuario");
            tokenDto.setEmail("usuario@email.com");
            tokenDto.setToken(token);

            Mockito.when(authenticationManager.authenticate(credentials)).thenReturn(authentication);
            Mockito.when(repository.findByEmail("usuario@email.com")).thenReturn(Optional.of(userAutenticado));
            Mockito.when(gerenciadorTokenJwt.generateToken(authentication)).thenReturn(token);

            UserTokenDto returnValue = service.autenticar(loginDto);

            assertEquals(1, returnValue.getUserId());
            assertEquals(user.getName(), returnValue.getNome());
            assertEquals(user.getEmail(), returnValue.getEmail());
            assertEquals(token, returnValue.getToken());

            Mockito.verify(repository, Mockito.times(1)).findByEmail(loginDto.getEmail());
            Mockito.verify(gerenciadorTokenJwt, Mockito.times(1)).generateToken(authentication);
        }

        @Test
        @DisplayName("F. Quando email ou senha está errado, deve retornar 401 - Unauthorized")
        void unauthorized() {
            UserLoginDto loginDto = new UserLoginDto();
            loginDto.setEmail("usuario@email.com");
            loginDto.setPassword("senha");

            UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                    "usuario@email.com", "senha"
            );

            Mockito.when(authenticationManager.authenticate(credentials)).thenThrow(new BadCredentialsException("Usuário inexistente ou senha inválida"));

            BadCredentialsException exception = assertThrows(
                    BadCredentialsException.class,
                    () -> service.autenticar(loginDto)
            );

            assertEquals("Usuário inexistente ou senha inválida", exception.getLocalizedMessage());
            Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(credentials);
        }
    }

    @Nested
    @DisplayName("getUserById()")
    public class getUserById {
        @Test
        @DisplayName("V. Quando id existe, deve retornar o usuário")
        void validId() {
            Integer id = 1;
            User user = UserMocks.getUser();

            Mockito.when(repository.findById(1)).thenReturn(Optional.of(user));

            User returnUser = service.getUserById(id);

            assertEquals(user.getId(), returnUser.getId());
            assertEquals(user.getName(), returnUser.getName());
            assertEquals(user.getEmail(), returnUser.getEmail());
            assertEquals(user.getCpf(), returnUser.getCpf());

            Mockito.verify(repository, Mockito.times(1)).findById(id);
        }

        @Test
        @DisplayName("F. Quando id não existe, deve lançar NotFoundException")
        void invalidId() {
            Integer id = 10;

            Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> service.getUserById(id)
            );

            assertEquals("Usuário não encontrado", exception.getLocalizedMessage());
            Mockito.verify(repository, Mockito.times(1)).findById(id);
        }
    }

    @Nested
    @DisplayName("getAccount()")
    public class getAccount {

        @Test
        @DisplayName("V. Quando token é valido, deve retornar usuário")
        void validToken() {
            String token = UserMocks.getToken();
            String email = "marcelo.soares@email.com";

            User user = UserMocks.getUser();

            Mockito.when(gerenciadorTokenJwt.getUsernameFromToken(token)).thenReturn(email);
            Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));

            User account = service.getAccount(token);

            assertEquals(user.getId(), account.getId());
            assertEquals(user.getName(), account.getName());
            assertEquals(user.getCpf(), account.getCpf());
            assertEquals(user.getEmail(), account.getEmail());
            assertEquals(user.getPassword(), account.getPassword());

            Mockito.verify(gerenciadorTokenJwt, Mockito.times(1)).getUsernameFromToken(token);
            Mockito.verify(repository, Mockito.times(1)).findByEmail(email);
        }

        @Test
        @DisplayName("F. Quando token é inválido, deve lançar NotFoundException")
        void invalidToken() {
            String token = UserMocks.getToken();
            String email = "marcelo.soares@email.com";


            Mockito.when(gerenciadorTokenJwt.getUsernameFromToken(token)).thenReturn(email);
            Mockito.when(repository.findByEmail(email)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> service.getAccount(token)
            );

            assertEquals("Usuário não encontrado", exception.getLocalizedMessage());

            Mockito.verify(gerenciadorTokenJwt, Mockito.times(1)).getUsernameFromToken(token);
            Mockito.verify(repository, Mockito.times(1)).findByEmail(email);
        }
    }

    @Nested
    @DisplayName("updateUser()")
    public class updateUser {

        @Test
        @DisplayName("V. Quando usuário possui dados válidos, atualiza usuário")
        void validData() {
            Integer id = 1;
            String token = UserMocks.getToken();
            User currentUser = UserMocks.getUser();
            User updatedUser = UserMocks.getUser();
            updatedUser.setName("João Silva");
            updatedUser.setEmail("joao.silva@email.com");
            updatedUser.setCpf("234.567.891-01");

            UserService spyService = Mockito.spy(service);

            Mockito.doReturn(currentUser).when(spyService).getUserById(id);
            Mockito.doReturn(currentUser).when(spyService).getAccount(token);
            Mockito.when(repository.existsByCpfAndIdNot(updatedUser.getCpf(), id)).thenReturn(false);
            Mockito.when(repository.existsByEmailAndIdNot(updatedUser.getEmail(), id)).thenReturn(false);
            Mockito.when(repository.save(updatedUser)).thenReturn(updatedUser);

            User returnedUser = spyService.updateUser(id, updatedUser, token);

            assertEquals(updatedUser.getId(), returnedUser.getId());
            assertEquals(updatedUser.getName(), returnedUser.getName());
            assertEquals(updatedUser.getEmail(), returnedUser.getEmail());
            assertEquals(updatedUser.getCpf(), returnedUser.getCpf());

            Mockito.verify(spyService, Mockito.times(1)).getUserById(id);
            Mockito.verify(spyService, Mockito.times(1)).getAccount(token);
            Mockito.verify(repository, Mockito.times(1)).existsByCpfAndIdNot(updatedUser.getCpf(), id);
            Mockito.verify(repository, Mockito.times(1)).existsByEmailAndIdNot(updatedUser.getEmail(), id);
            Mockito.verify(repository, Mockito.times(1)).save(updatedUser);
        }

        @Test
        @DisplayName("F. Quando usuário não é o mesmo que usuário logado, deve lançar ForbiddenException")
        void differentUsers() {
            Integer id = 1;
            String token = UserMocks.getToken();
            User currentUser = UserMocks.getUser();
            User loggedUser = UserMocks.getUser();
            loggedUser.setId(2);
            loggedUser.setName("João Silva");
            loggedUser.setEmail("joao.silva@email.com");
            loggedUser.setCpf("234.567.891-01");

            UserService spyService = Mockito.spy(service);

            Mockito.doReturn(currentUser).when(spyService).getUserById(id);
            Mockito.doReturn(loggedUser).when(spyService).getAccount(token);

            ForbiddenException exception = assertThrows(
                    ForbiddenException.class,
                    () -> spyService.updateUser(id, currentUser, token)
            );

            assertEquals("Você não tem permissão para fazer esta ação", exception.getLocalizedMessage());
        }

        @Test
        @DisplayName("F. Quando CPF já existir em outro usuário, deve lançar ConflitoException")
        void cpfAlreadyExists() {
            Integer id = 1;
            String token = UserMocks.getToken();
            User currentUser = UserMocks.getUser();
            UserService spyService = Mockito.spy(service);

            Mockito.doReturn(currentUser).when(spyService).getUserById(id);
            Mockito.doReturn(currentUser).when(spyService).getAccount(token);
            Mockito.when(repository.existsByCpfAndIdNot(currentUser.getCpf(), id)).thenReturn(true);

            ConflictException exception = assertThrows(
                    ConflictException.class,
                    () -> spyService.updateUser(id, currentUser, token)
            );

            assertEquals("Já existe um usuário com este CPF", exception.getLocalizedMessage());
        }

        @Test
        @DisplayName("F. Quando e-mail já existir em outro usuário, deve retornar ConflitoException")
        void emailAlreadyExists() {
            Integer id = 1;
            String token = UserMocks.getToken();
            User currentUser = UserMocks.getUser();
            UserService spyService = Mockito.spy(service);

            Mockito.doReturn(currentUser).when(spyService).getUserById(id);
            Mockito.doReturn(currentUser).when(spyService).getAccount(token);
            Mockito.when(repository.existsByCpfAndIdNot(currentUser.getCpf(), id)).thenReturn(false);
            Mockito.when(repository.existsByEmailAndIdNot(currentUser.getEmail(), id)).thenReturn(true);

            ConflictException exception = assertThrows(
                    ConflictException.class,
                    () -> spyService.updateUser(id, currentUser, token)
            );

            assertEquals("Já existe um usuário com este e-mail", exception.getLocalizedMessage());
        }
    }

    @Nested
    @DisplayName("deleteUser()")
    public class deleteUser {
        @Test
        @DisplayName("V. Quando id existe, deve executar delete()")
        void validId() {
            Integer id = 1;
            User user = UserMocks.getUser();

            UserService spyService = Mockito.spy(service);

            Mockito.doReturn(user).when(spyService).getUserById(id);

            spyService.deleteUser(id);

            Mockito.verify(repository, Mockito.times(1)).delete(user);
            Mockito.verify(spyService, Mockito.times(1)).getUserById(id);
        }

        @Test
        @DisplayName("F. Quando id não existe, deve lançar NotFoundException")
        void invalidId() {
            Integer id = 10;

            UserService spyService = Mockito.spy(service);

            Mockito.doThrow(new NotFoundException("Usuário não encontrado"))
                    .when(spyService).getUserById(id);

            NotFoundException exception = assertThrows(
                    NotFoundException.class,
                    () -> spyService.deleteUser(id)
            );

            assertEquals("Usuário não encontrado", exception.getLocalizedMessage());
            Mockito.verify(spyService, Mockito.times(1)).getUserById(id);
        }
    }
}