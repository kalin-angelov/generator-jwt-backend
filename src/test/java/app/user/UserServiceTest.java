package app.user;

import app.exceptions.EmailExistInDatabaseException;
import app.exceptions.UsernameExistInDatabaseException;
import app.jwt.JwtService;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.EditRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void givenExistingEmailInDb_whenRegister_thenExceptionIsThrown() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("MyMail@gmail.com")
                .username("username")
                .password("password")
                .build();

        User existingUser = User.builder()
                .email("MyMail@gmail.com")
                .build();

        User newUser = User.builder().build();

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(EmailExistInDatabaseException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(newUser);

    }

    @Test
    void givenExistingUsernameInDatabase_whenRegister_theExceptionIsThrown() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("MyEmail@gmail.com")
                .username("usernameIsTaken")
                .password("password")
                .build();

        User existingUser = User.builder()
                .username("usernameIsTaken")
                .build();

        User newUser = User.builder().build();

        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(existingUser));

        assertThrows(UsernameExistInDatabaseException.class, () -> userService.register(registerRequest));
        verify(userRepository, never()).save(newUser);
    }

    @Test
    void givenHappyPath_whenRegister() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("newUserEmail@gmail.com")
                .username("newUser")
                .password("password")
                .build();

        User newUser = User.builder().build();

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(newUser);

        userService.register(registerRequest);
    }

    @Test
    void givenExistingEmailInDatabase_whenEditUser_thenExceptionIsThrown() {

        UUID id = UUID.randomUUID();
        EditRequest editRequest = EditRequest.builder()
                .email("email@gmail.com")
                .build();

        User existingUser = User.builder()
                .email("email@gmail.com")
                .build();

        User user = User.builder()
                .email("oldEmail@gmail.com")
                .build();

        when(userRepository.findByEmail(editRequest.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertThrows(EmailExistInDatabaseException.class, () -> userService.editUser(id, editRequest));
        verify(userRepository, never()).save(user);
    }

    @Test
    void givenHappyPath_whenEditUser() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        EditRequest editRequest = EditRequest.builder()
                .email("newEmail@gmail.com")
                .firstName("first")
                .lastName("last")
                .build();

        when(userRepository.findByEmail(editRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.editUser(user.getId(), editRequest);

        assertEquals(user.getEmail(), editRequest.getEmail());
        assertEquals(user.getFirstName(), editRequest.getFirstName());
        assertEquals(user.getLastName(), editRequest.getLastName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenEmptyFirstName_whenEditUser_thenRemoveFirstNameFromUserInfo() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        EditRequest editRequest = EditRequest.builder()
                .email("newEmail@gmail.com")
                .firstName(null)
                .build();

        when(userRepository.findByEmail(editRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.editUser(user.getId(), editRequest);

        assertNull(user.getFirstName());
    }

    @Test
    void givenEmptyLastName_whenEditUser_thenRemoveLastNameFromUserInfo() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        EditRequest editRequest = EditRequest.builder()
                .email("newEmail@gmail.com")
                .lastName(null)
                .build();

        when(userRepository.findByEmail(editRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.editUser(user.getId(), editRequest);

        assertNull(user.getLastName());
    }
}
