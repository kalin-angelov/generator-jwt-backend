package app;

import app.user.model.User;
import app.user.model.UserRole;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class TestBuilder {

    public static User aRandomUser() {

       return User.builder()
                .email("randomEmail@gmail.com")
                .username("randomUsername")
                .password("randomPassword")
                .role(UserRole.USER)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static RegisterRequest aRandomRegisterRequest() {

        return RegisterRequest.builder()
                .email("email@gmail.com")
                .username("username")
                .password("password")
                .build();

    }

    public static LoginRequest aRandomLoginRequest() {

        return LoginRequest.builder()
                .username("username")
                .password("password")
                .build();

    }
}
