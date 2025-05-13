package app.web;

import app.exceptions.EmailExistInDatabaseException;
import app.exceptions.InvalidUsernameOrPasswordException;
import app.exceptions.UsernameExistInDatabaseException;
import app.jwt.JwtService;
import app.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static app.TestBuilder.aRandomLoginRequest;
import static app.TestBuilder.aRandomRegisterRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
public class AuthControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postRequestToRegisterEndpoint_happyPath() throws Exception {

        MockHttpServletRequestBuilder request = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomRegisterRequest()));

        mockMvc.perform(request)
                .andExpect(status().is(201))
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("successful").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());

        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpoint_withExistingEmailInDatabase() throws Exception {

        when(userService.register(any())).thenThrow(new EmailExistInDatabaseException());

        MockHttpServletRequestBuilder request = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomRegisterRequest()));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());
    }

    @Test
    void postRequestToRegisterEndpoint_withExistingUsernameInDatabase() throws Exception {

        when(userService.register(any())).thenThrow(new UsernameExistInDatabaseException());

        MockHttpServletRequestBuilder request = post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomRegisterRequest()));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());
    }

    @Test
    void postRequestToLoginEndpoint_whenTheUsernameOrPasswordIsInvalid() throws Exception {

        when(userService.verify(any())).thenThrow(new InvalidUsernameOrPasswordException());

        MockHttpServletRequestBuilder request = post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomLoginRequest()));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());
    }

    @Test
    void postRequestToLoginEndpoint_happyPath() throws Exception{

        String fakeToken = "this is my face toke";

        when(userService.verify(aRandomLoginRequest())).thenReturn(fakeToken);

        MockHttpServletRequestBuilder request = post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(aRandomLoginRequest()));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("successful").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty())
                .andExpect(jsonPath("token").isNotEmpty());

        verify(userService, times(1)).verify(aRandomLoginRequest());
    }
}
