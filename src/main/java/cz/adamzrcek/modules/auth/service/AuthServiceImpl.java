package cz.adamzrcek.modules.auth.service;

import cz.adamzrcek.modules.auth.dtos.AuthResponse;
import cz.adamzrcek.modules.auth.dtos.LoginRequest;
import cz.adamzrcek.modules.auth.dtos.RegisterRequest;
import cz.adamzrcek.modules.auth.dtos.TokenRefreshRequest;
import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.user.entity.UserDetail;
import cz.adamzrcek.modules.auth.exception.EmailAlreadyExistsException;
import cz.adamzrcek.modules.auth.exception.InvalidPasswordException;
import cz.adamzrcek.modules.user.exception.UserNotFoundException;
import cz.adamzrcek.modules.referencedata.repository.RoleRepository;
import cz.adamzrcek.modules.user.repository.UserDetailRepository;
import cz.adamzrcek.modules.user.repository.UserRepository;
import cz.adamzrcek.modules.auth.security.JwtBlacklist;
import cz.adamzrcek.modules.auth.security.JwtUtil;
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
    private final UserDetailRepository userDetailRepository;

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("email already exists");
        }
        if (userRepository.findByUsernameIgnoreCase(registerRequest.getUsername()).isPresent()) {
            throw new EmailAlreadyExistsException("username already exists");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .userDetail(addUserDetail(registerRequest))
                .role(roleRepository.findByName("USER"))
                .build();

        userRepository.save(user);

        log.debug("User {} registered successfully", user.getUsername());
    }

    private UserDetail addUserDetail(RegisterRequest registerRequest) {
        UserDetail userDetail = UserDetail.builder()
                .lastName(registerRequest.getLastName())
                .firstName(registerRequest.getFirstName())
                .birthDate(registerRequest.getBirthDate())
                .build();

        return userDetailRepository.save(userDetail);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user;

        if (request.getIdentifier().contains("@")) {
            user = userRepository.findByEmail(request.getIdentifier())
                    .orElseThrow(() -> new UserNotFoundException("User does not exists"));
        }else {
            user = userRepository.findByUsernameIgnoreCase(request.getIdentifier())
                    .orElseThrow(() -> new UserNotFoundException("User does not exists"));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().getName());
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

        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().getName());
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
