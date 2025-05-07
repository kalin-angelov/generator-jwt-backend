package app.web;

import app.user.service.UserService;
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

        userService.register(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MessageResponse.builder()
                    .status(HttpStatus.CREATED.value())
                    .successful(true)
                    .message("User successfully created.")
                    .build());
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@RequestBody LoginRequest loginRequest) {

        String token = userService.verify(loginRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .status(HttpStatus.OK.value())
                        .successful(true)
                        .message("Welcome")
                        .token(token)
                        .build());
    }
}
