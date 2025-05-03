package app.web.mapper;

import app.web.dto.RegisterResponse;
import app.web.dto.LoginRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public RegisterResponse toRegisterResponse(LoginRequest loginRequest, String message) {

        return RegisterResponse.builder()
                .message(message)
                .build();
    }
}
