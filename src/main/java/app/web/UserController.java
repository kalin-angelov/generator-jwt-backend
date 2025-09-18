package app.web;

import app.user.model.User;
import app.user.model.UserPrincipal;
import app.user.service.UserService;
import app.web.dto.ChangePasswordRequest;
import app.web.dto.EditRequest;
import app.web.dto.MessageResponse;
import app.web.dto.UserResponse;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    private ResponseEntity<UserResponse> getUserInfo (@AuthenticationPrincipal UserPrincipal userPrincipal) {
        User user = userPrincipal.getUser();
        UserResponse response = DtoMapper.toUserResponse(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<UserResponse> editUser(@RequestParam(name = "userId") UUID userId, @RequestBody EditRequest editRequest) {

        User user = userService.editUser(userId, editRequest);
        UserResponse response = DtoMapper.toUserResponse(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<MessageResponse> changePassword(@RequestParam(name = "userId") UUID userId, @RequestBody ChangePasswordRequest changePasswordRequest) {

        User user = userService.getUser(userId);
        userService.changePassword(user, changePasswordRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(MessageResponse.builder()
                        .status(HttpStatus.OK.value())
                        .successful(true)
                        .message("Password changed successfully.")
                        .build());
    }
}
