package app.web.dto;

import app.user.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private int status;
    private User user;

}
