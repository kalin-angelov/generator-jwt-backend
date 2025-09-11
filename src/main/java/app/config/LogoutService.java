package app.config;

import app.token.model.Token;
import app.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService  implements LogoutHandler {

    private final TokenService tokenService;

    @Autowired
    public LogoutService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String requestHeader = request.getHeader("Authorization");
        final String token;

        if (requestHeader == null || !requestHeader.startsWith("Bearer ")) {
            return;
        }

        token = requestHeader.substring(7);
        Token storedToken = tokenService.findByToken(token).orElse(null);

        if (storedToken != null) {
            tokenService.revokeToken(storedToken);
        }
    }
}
