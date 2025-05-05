package app.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditRequest {

    private String email;
    private String imgUrl;
    private String firstName;
    private String lastName;

}
