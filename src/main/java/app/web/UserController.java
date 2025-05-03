package app.web;

import app.user.model.User;
import app.user.model.UserPrincipal;
import app.user.service.UserService;
import app.web.dto.EditRequest;
import app.web.dto.UserResponse;
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

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserResponse.builder()
                        .status(HttpStatus.OK.value())
                        .user(user)
                        .build());
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<UserResponse> editUser(@PathVariable UUID id, @RequestBody EditRequest editRequest) {

        User updatedUser = userService.editUser(id, editRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserResponse.builder()
                        .status(HttpStatus.OK.value())
                        .user(updatedUser)
                        .build());
    }
}
