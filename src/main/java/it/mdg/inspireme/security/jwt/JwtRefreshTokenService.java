package it.mdg.inspireme.security.jwt;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtRefreshTokenService {
    private final Map<String, String> refreshTokenStore = new HashMap<>();

    // Genera un nuovo refresh token
    public String createRefreshToken(String username) {
        String refreshToken = UUID.randomUUID().toString();
        refreshTokenStore.put(username, refreshToken);
        return refreshToken;
    }

    // Verifica se il refresh token è valido
    public boolean validateRefreshToken(String username, String refreshToken) {
        return refreshToken.equals(refreshTokenStore.get(username));
    }

    // Rimuove il refresh token
    public void deleteRefreshToken(String username) {
        refreshTokenStore.remove(username);
    }
}
