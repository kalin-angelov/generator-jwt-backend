package app.web.mapper;

import app.user.model.User;
import app.web.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DtoMapperTest {

    @Test
    void givenHappyPath_whenMappingToUserResponse() {

        User user = User.builder()
                .email("myEmail@gmail.com")
                .username("myUsername")
                .password("myPassword")
                .build();

        UserResponse userResponse = DtoMapper.toUserResponse(user);

        assertEquals(user.getUsername(), userResponse.getUsername());
        assertEquals(user.getEmail(), userResponse.getEmail());;
    }
}
