package app.web.dto;

import app.user.model.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private UUID id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String imgUrl;
    private UserRole role;
    private boolean isActive;

}
