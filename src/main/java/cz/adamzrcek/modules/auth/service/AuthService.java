package cz.adamzrcek.modules.auth.service;

import cz.adamzrcek.modules.auth.dtos.AuthResponse;
import cz.adamzrcek.modules.auth.dtos.LoginRequest;
import cz.adamzrcek.modules.auth.dtos.RegisterRequest;
import cz.adamzrcek.modules.auth.dtos.TokenRefreshRequest;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(TokenRefreshRequest request);
    void logout(String token);
}
