package app.web;

import app.user.service.UserService;
import app.web.dto.LoginResponse;
import app.web.dto.RegisterRequest;
import app.web.dto.MessageResponse;
import app.web.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterRequest registerRequest) {

        String message = userService.register(registerRequest);

        if (message.contains("taken")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(MessageResponse.builder()
                        .status(HttpStatus.CONFLICT.value())
                        .successful(false)
                        .message(message)
                        .build());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MessageResponse.builder()
                    .status(HttpStatus.CREATED.value())
                    .successful(true)
                    .message(message)
                    .build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        String result = userService.verify(loginRequest);

        if (result.startsWith("Invalid")) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(LoginResponse.builder()
                            .status(HttpStatus.FORBIDDEN.value())
                            .successful(false)
                            .message("Invalid username or password.")
                            .build());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(LoginResponse.builder()
                        .status(HttpStatus.OK.value())
                        .successful(true)
                        .message("Welcome")
                        .token(result)
                        .build());
    }
}
