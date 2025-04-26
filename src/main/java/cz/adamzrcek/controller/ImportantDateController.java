package cz.adamzrcek.controller;

import cz.adamzrcek.dtos.importantDate.ImportantDateDto;
import cz.adamzrcek.dtos.importantDate.ImportantDateRequest;
import cz.adamzrcek.service.ImportantDateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/important-dates")
@RequiredArgsConstructor
public class ImportantDateController {
    private final ImportantDateService importantDateService;

    @Operation(summary = "Create a new important date", responses = {
            @ApiResponse(responseCode = "201", description = "Important date created"),
            @ApiResponse(responseCode = "500", description = "Internal Error"),
    })
    @PostMapping
    public ResponseEntity<ImportantDateDto> createImportantDate(@RequestBody ImportantDateRequest request) {
        var response = importantDateService.createImportantDate(request);

        URI location = URI.create(String.format("/api/v1/important-dates/%d", response.id()));
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Get all important dates", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error"),
    })
    @GetMapping
    public ResponseEntity<List<ImportantDateDto>> getAllImportantDates() {
        return ResponseEntity.ok(importantDateService.getAllImportantDates());
    }

    @Operation(summary = "Get important date by id", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ImportantDateDto> getImportantDate(@PathVariable Long id) {
        return ResponseEntity.ok(importantDateService.getImportantDate(id));
    }

    @Operation(summary = "Update important date", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<ImportantDateDto> updateImportantDate(@PathVariable Long id,
                                                                @RequestBody ImportantDateRequest request) {
        return ResponseEntity.ok(importantDateService.updateImportantDate(id, request));
    }

    @Operation(summary = "Delete important date", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImportantDate(@PathVariable Long id) {
        importantDateService.deleteImportantDate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all categories", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @GetMapping("/category")
    public ResponseEntity<Map<Long, String>> listCategories(){
        Map<Long, String> response = importantDateService.getAllCategoriesAsMap();
        return ResponseEntity.ok(response);
    }
}
