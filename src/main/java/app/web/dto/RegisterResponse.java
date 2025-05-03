package app.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private int status;
    private boolean successful;
    private String message;
}
