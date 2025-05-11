package app.web;

import app.exceptions.EmailExistInDatabaseException;
import app.jwt.JwtService;
import app.user.model.User;
import app.user.model.UserPrincipal;
import app.user.model.UserRole;
import app.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static app.TestBuilder.aRandomEditRequest;
import static app.TestBuilder.aRandomUser;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToProfileEndPoint_happyPath() throws Exception {

        UserPrincipal userPrincipal = new UserPrincipal(aRandomUser());

        MockHttpServletRequestBuilder request = get("/api/v1/users/profile")
                .with(authentication(new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities())))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").isNotEmpty())
                .andExpect(jsonPath("email").isNotEmpty());
    }

    @Test
    void putRequestToEditEndpoint_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .email(aRandomUser().getEmail())
                .firstName(aRandomUser().getFirstName())
                .lastName(aRandomUser().getLastName())
                .imgUrl(aRandomUser().getImgUrl())
                .build();

        when(userService.editUser(userId, aRandomEditRequest())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/api/v1/users/{id}/edit", userId)
                .with(user("username").roles(UserRole.USER.toString()))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomEditRequest()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").isNotEmpty())
                .andExpect(jsonPath("firstName").isNotEmpty())
                .andExpect(jsonPath("lastName").isNotEmpty())
                .andExpect(jsonPath("imgUrl").isNotEmpty());

        verify(userService, times(1)).editUser(userId, aRandomEditRequest());
    }

    @Test
    void putRequestToEditEndpoint_wheTryToEditEmailWithExistingEmailInDatabase() throws Exception {

        UUID userId = UUID.randomUUID();

        when(userService.editUser(userId, aRandomEditRequest())).thenThrow(new EmailExistInDatabaseException());

        MockHttpServletRequestBuilder request = put("/api/v1/users/{id}/edit", userId)
                .with(user("username").roles(UserRole.USER.toString()))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomEditRequest()));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty())
                .andExpect(jsonPath("timestamp").isNotEmpty());
    }
}
