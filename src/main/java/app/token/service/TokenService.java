package app.token.service;

import app.token.model.Token;
import app.token.model.TokenType;
import app.token.repository.TokenRepository;
import app.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void initializeToken(User user, String token) {

        Token generatedToken = Token.builder()
                .token(token)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .owner(user)
                .build();

        tokenRepository.save(generatedToken);
    }

    public void revokedAllUserTokens(User user) {

        List<Token> allUnexpiredAndUnRevokedTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (allUnexpiredAndUnRevokedTokens.isEmpty()) {
            return;
        }

        allUnexpiredAndUnRevokedTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(allUnexpiredAndUnRevokedTokens);
    }

    public Optional<Token> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void revokeToken(Token token) {
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
    }
}
