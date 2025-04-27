package cz.adamzrcek.service.impl;

import cz.adamzrcek.dtos.authorization.AuthResponse;
import cz.adamzrcek.dtos.authorization.LoginRequest;
import cz.adamzrcek.dtos.authorization.RegisterRequest;
import cz.adamzrcek.dtos.authorization.TokenRefreshRequest;
import cz.adamzrcek.entity.User;
import cz.adamzrcek.exception.EmailAlreadyExistsException;
import cz.adamzrcek.exception.InvalidPasswordException;
import cz.adamzrcek.exception.UserNotFoundException;
import cz.adamzrcek.repository.RoleRepository;
import cz.adamzrcek.repository.UserRepository;
import cz.adamzrcek.security.JwtBlacklist;
import cz.adamzrcek.security.JwtUtil;
import cz.adamzrcek.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtBlacklist jwtBlacklist;

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("email already exists");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .birthDate(registerRequest.getBirthDate())
                .role(roleRepository.findByName("USER"))
                .build();

        userRepository.save(user);

        log.debug("User {} registered successfully", user.getUsername());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user;

        if (request.getIdentifier().contains("@")) {
            user = userRepository.findByEmail(request.getIdentifier())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }else {
            user = userRepository.findByUsernameIgnoreCase(request.getIdentifier())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
        log.debug("User {} logged in", user.getUsername());
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token has expired");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().getName());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        log.debug("User {} refreshed token", user.getUsername());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public void logout(String token) {
        try {
            Claims claims = jwtUtil.getClaimsFromToken(token);

            Instant expiryTime = claims.getExpiration().toInstant();

            jwtBlacklist.blacklistToken(token, expiryTime);

            log.debug("User with email {} logged out and token blacklisted", claims.getSubject());
        } catch (Exception e) {
            log.warn("Failed to process logout for token: {}", e.getMessage());
        }
    }
}
