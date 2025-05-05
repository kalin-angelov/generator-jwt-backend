package app.web.mapper;

import app.user.model.User;
import app.web.dto.UserResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public UserResponse toUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .imgUrl(user.getImgUrl())
                .isActive(user.isActive())
                .role(user.getRole())
                .build();
    }
}
