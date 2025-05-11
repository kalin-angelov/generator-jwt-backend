package app;

import app.user.model.User;
import app.user.model.UserRole;
import app.web.dto.EditRequest;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class TestBuilder {

    public static User aRandomUser() {

       return User.builder()
               .id(UUID.randomUUID())
               .email("randomEmail@gmail.com")
               .username("randomUsername")
               .password("randomPassword")
               .firstName("randomFirstName")
               .lastName("randomLastName")
               .imgUrl("randomImgUrl")
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

    public static EditRequest aRandomEditRequest() {

        return EditRequest.builder()
                .email("email@gmail.com")
                .firstName("firstName")
                .lastName("lastName")
                .imgUrl("imgUrl")
                .build();
    }
}
