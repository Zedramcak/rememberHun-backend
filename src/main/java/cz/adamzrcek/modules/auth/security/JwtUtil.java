package cz.adamzrcek.modules.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

    private static final long TOKEN_EXPIRATION_TIME_MS = 3600000;
    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(Long userId, String email, String role) {

        String tokenId = UUID.randomUUID().toString();

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("email", email)
                .claim("id", userId)
                .setId(tokenId)
                .setIssuer("rememberHun-api")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME_MS)) // 1 hour
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return getClaimsFromToken(token).getSubject();

    }

    public Long extractId(String token) {
        Object id = getClaimsFromToken(token).get("id");
        if (id instanceof Integer) {
            return ((Integer) id).longValue();
        } else if (id instanceof Long) {
            return (Long) id;
        } else if (id instanceof String) {
            return Long.parseLong((String) id);
        }
        return null;
    }

    public String extractRole(String token) {
        return (String) getClaimsFromToken(token).get("role");
    }

    public boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, String expectedEmail) {
        final String email = extractEmail(token);
        return (email.equals(expectedEmail)) && !isTokenExpired(token);
    }

    private SecretKey getSecretKey() {
        return key;
    }
}
