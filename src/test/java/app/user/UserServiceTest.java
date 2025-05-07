package app.user;

import app.exceptions.EmailExistInDatabaseException;
import app.jwt.JwtService;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
}
