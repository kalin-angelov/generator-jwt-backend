package app.user.service;

import app.exceptions.EmailExistInDatabaseException;
import app.exceptions.InvalidUsernameOrPasswordException;
import app.exceptions.UsernameExistInDatabaseException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        Optional<User> optionalUserFindByEmail = this.findUserByEmail(registerRequest.getEmail());

        if (optionalUserFindByEmail.isPresent()) {
            log.info("User with email [%s] already exist.".formatted(registerRequest.getEmail()));
            throw new EmailExistInDatabaseException();
        }

        if (optionalUserFindByUsername.isPresent()) {
            log.info("User with username [%s] already exist.".formatted(registerRequest.getUsername()));
            throw new UsernameExistInDatabaseException();
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
        return jwtService.generateToken(user.getUsername());

    }

    public String verify(LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User with username [%s] not found.".formatted(loginRequest.getUsername())));

            return jwtService.generateToken(user.getUsername());
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        throw new InvalidUsernameOrPasswordException();
    }

    public User editUser(UUID id, EditRequest editRequest) {

        User user = this.getUser(id);
        Optional<User> optionalUserFindByEmail = this.findUserByEmail(editRequest.getEmail());

        if (optionalUserFindByEmail.isPresent() && !user.getEmail().equals(optionalUserFindByEmail.get().getEmail()) || editRequest.getEmail().isBlank()) {
            throw new EmailExistInDatabaseException();
        }

        user.setEmail(editRequest.getEmail());
        user.setFirstName(editRequest.getFirstName());
        user.setLastName(editRequest.getLastName());
        user.setImgUrl(editRequest.getImgUrl());

        userRepository.save(user);
        log.info("User with id [%s] successfully updated".formatted(user.getId()));
        return user;
    }

    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    private Optional<User> findUserByEmail (String email) {
        return userRepository.findByEmail(email);
    }
}
