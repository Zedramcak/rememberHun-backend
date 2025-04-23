package cz.adamzrcek.service;

import cz.adamzrcek.dtos.authorization.LoginRequest;
import cz.adamzrcek.dtos.authorization.RegisterRequest;
import cz.adamzrcek.dtos.authorization.TokenRefreshRequest;
import cz.adamzrcek.entity.User;
import cz.adamzrcek.exception.EmailAlreadyExistsException;
import cz.adamzrcek.exception.InvalidPasswordException;
import cz.adamzrcek.exception.UserNotFoundException;
import cz.adamzrcek.repository.UserRepository;
import cz.adamzrcek.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;


    @Test
    public void userRegisterOkTest(){
        var registerRequest = new RegisterRequest("username","email", "password", null, null, null);
        given(userRepository.findByEmail(anyString())).willReturn(java.util.Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("password");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        authService.register(registerRequest);

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        var user = userArgumentCaptor.getValue();

        assertAll(
            () -> assertEquals(registerRequest.getUsername(), user.getUsername()),
            () -> assertEquals(registerRequest.getEmail(), user.getEmail()),
            () -> assertEquals("password", user.getPassword()),
            () -> assertEquals("USER", user.getRole().name())
        );
    }

    @Test
    public void userRegisterFailTest(){
        var registerRequest = new RegisterRequest("username","email", "password", null, null, null);

        given(userRepository.findByEmail(anyString())).willReturn(java.util.Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(registerRequest));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void userLoginWithUsernameOkTest(){
        var loginRequest = new LoginRequest("username", "password");
        var user = TestUserFactory.basicUser();


        given(userRepository.findByUsernameIgnoreCase(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtUtil.generateToken(anyString(), anyString())).willReturn("token");
        given(jwtUtil.generateRefreshToken(anyString())).willReturn("refreshToken");

        var response = authService.login(loginRequest);

        verify(userRepository, times(1)).findByUsernameIgnoreCase(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());

        verify(userRepository, never()).findByEmail(anyString());

        assertAll(
            () -> assertEquals("username", response.getUsername()),
            () -> assertEquals("USER", response.getRole()),
            () -> assertEquals("token", response.getToken()),
            () -> assertEquals("refreshToken", response.getRefreshToken())
        );
    }

    @Test
    public void userLoginWithEmailOkTest(){
        var loginRequest = new LoginRequest("email@email.com", "password");
        var user = TestUserFactory.basicUser();

        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtUtil.generateToken(anyString(), anyString())).willReturn("token");
        given(jwtUtil.generateRefreshToken(anyString())).willReturn("refreshToken");

        var response = authService.login(loginRequest);

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());

        verify(userRepository, never()).findByUsernameIgnoreCase(anyString());

        assertAll(
                () -> assertEquals("username", response.getUsername()),
                () -> assertEquals("USER", response.getRole()),
                () -> assertEquals("token", response.getToken()),
                () -> assertEquals("refreshToken", response.getRefreshToken())
        );
    }

    @Test
    public void userLoginFailTest(){
        var loginRequest = new LoginRequest("username", "password");
        var user = TestUserFactory.basicUser();

        assertThrows(UserNotFoundException.class, () -> authService.login(loginRequest));

        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
        verify(jwtUtil, never()).extractEmail(anyString());
        verify(jwtUtil, never()).isTokenExpired(anyString());
    }

    @Test
    public void userLoginIncorrectPasswordTest(){
        var loginRequest = new LoginRequest("username", "password");
        var user = TestUserFactory.basicUser();
        given(userRepository.findByUsernameIgnoreCase(anyString())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        assertThrows(InvalidPasswordException.class, () -> authService.login(loginRequest));
        verify(userRepository, never()).findByEmail(anyString());
        verify(passwordEncoder, atMostOnce()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
        verify(jwtUtil, never()).generateRefreshToken(anyString());
        verify(jwtUtil, never()).extractEmail(anyString());
        verify(jwtUtil, never()).isTokenExpired(anyString());
    }

    @Test
    public void refreshTokenOkTest(){
        var request = new TokenRefreshRequest("refreshToken");
        var email = "email@email.com";
        var user = TestUserFactory.basicUser();

        given(jwtUtil.isTokenExpired(anyString())).willReturn(false);
        given(jwtUtil.extractEmail(anyString())).willReturn(email);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        given(jwtUtil.generateToken(anyString(), anyString())).willReturn("token");
        given(jwtUtil.generateRefreshToken(anyString())).willReturn("refreshToken");

        var response = authService.refreshToken(request);

        verify(jwtUtil, times(1)).isTokenExpired(anyString());
        verify(jwtUtil, times(1)).extractEmail(anyString());
        verify(userRepository, times(1)).findByEmail(anyString());

        assertAll(
            () -> assertEquals("token", response.getToken()),
            () -> assertEquals("refreshToken", response.getRefreshToken())
        );

    }

    @Test
    public void refreshTokenExpiredTest(){
        var request = new TokenRefreshRequest("refreshToken");
        given(jwtUtil.isTokenExpired(anyString())).willReturn(true);

        assertThrows(RuntimeException.class, () -> authService.refreshToken(request));

        verify(jwtUtil, times(1)).isTokenExpired(anyString());
        verify(jwtUtil, never()).extractEmail(anyString());
        verify(userRepository, never()).findByEmail(anyString());
    }

}