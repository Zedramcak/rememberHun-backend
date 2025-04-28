package cz.adamzrcek.modules.user.controller;

import cz.adamzrcek.modules.user.dtos.UserResponse;
import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get current user")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(){
        User user = userService.getCurrentUser();

        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getName(),
                user.getUserDetail().getFirstName(),
                user.getUserDetail().getLastName(),
                user.getUserDetail().getBirthDate()
        );

        return ResponseEntity.ok(response);
    }
}
