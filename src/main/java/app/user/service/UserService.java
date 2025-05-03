package app.user.service;

import app.jwt.JwtService;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.EditRequest;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public String register(RegisterRequest registerRequest) {

        Optional<User> optionalUserFindByUsername = userRepository.findByUsername(registerRequest.getUsername());
        Optional<User> optionalUserFindByEmail = userRepository.findByEmail(registerRequest.getEmail());

        if (optionalUserFindByEmail.isPresent()) {
            log.info("User with email [%s] already exist.".formatted(registerRequest.getEmail()));
            return "Email is taken";
        }

        if (optionalUserFindByUsername.isPresent()) {
            log.info("User with username [%s] already exist.".formatted(registerRequest.getUsername()));
            return "Username is taken";
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("User with id [%s] and username [%s] successfully created".formatted(user.getId(), user.getUsername()));
        return "User successfully created";

    }

    public String verify(LoginRequest loginRequest) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(loginRequest.getUsername());
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return "Invalid username or password.";
    }

    public User editUser(UUID id, EditRequest editRequest) {

        User user = userRepository.findById(id).orElseThrow();

        user.setEmail(editRequest.getEmail());
        user.setUsername(editRequest.getUsername());
        user.setImgUrl(editRequest.getImgUrl());

        userRepository.save(user);
        log.info("User with id [%s] successfully updated".formatted(user.getId()));
        return user;
    }
}
