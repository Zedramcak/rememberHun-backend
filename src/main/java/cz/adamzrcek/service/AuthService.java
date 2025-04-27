package cz.adamzrcek.service;

import cz.adamzrcek.dtos.authorization.AuthResponse;
import cz.adamzrcek.dtos.authorization.LoginRequest;
import cz.adamzrcek.dtos.authorization.RegisterRequest;
import cz.adamzrcek.dtos.authorization.TokenRefreshRequest;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(TokenRefreshRequest request);
    void logout(String token);
}
