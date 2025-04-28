package cz.adamzrcek.modules.connection.controller;

import cz.adamzrcek.dtos.ErrorResponse;
import cz.adamzrcek.modules.connection.dtos.ConnectionAcceptRequest;
import cz.adamzrcek.modules.connection.dtos.ConnectionDeleteRequest;
import cz.adamzrcek.modules.connection.dtos.ConnectionDto;
import cz.adamzrcek.modules.connection.dtos.ConnectionNewRequest;
import cz.adamzrcek.modules.connection.dtos.ConnectionSignedUserResponse;
import cz.adamzrcek.service.ConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/connection")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Operation(summary = "Get current connection", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "NOT ALLOWED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ConnectionSignedUserResponse> getCurrentConnection() {
        return ResponseEntity.ok(connectionService.getCurrentConnection());
    }

    @Operation(summary = "Request connection", responses = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "NOT ALLOWED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/request")
    public ResponseEntity<ConnectionDto> requestConnection(@RequestBody ConnectionNewRequest request) {
        var newConnection = connectionService.createNewConnection(request);

        URI location = URI.create("/api/v1/connection");

        return ResponseEntity.created(location).body(newConnection);
    }

    @Operation(summary = "Accept connection", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "NOT ALLOWED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/accept")
    public ResponseEntity<ConnectionDto> acceptConnection(@RequestBody ConnectionAcceptRequest request) {
        return ResponseEntity.ok(connectionService.acceptConnection(request));
    }

    @Operation(summary = "Delete connection", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "NOT ALLOWED", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteConnection(@RequestBody ConnectionDeleteRequest request) {
        connectionService.deleteConnection(request);
        return ResponseEntity.noContent().build();
    }
}
