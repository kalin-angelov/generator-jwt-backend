package app.web;

import app.user.model.User;
import app.user.model.UserPrincipal;
import app.user.service.UserService;
import app.web.dto.EditRequest;
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

    @PutMapping("/{id}/edit")
    public ResponseEntity<UserResponse> editUser(@PathVariable UUID id, @RequestBody EditRequest editRequest) {

        User user = userService.editUser(id, editRequest);
        UserResponse response = DtoMapper.toUserResponse(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
