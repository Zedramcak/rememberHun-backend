package cz.adamzrcek.modules.preference.controller;

import cz.adamzrcek.modules.preference.dtos.PreferenceDto;
import cz.adamzrcek.modules.preference.dtos.PreferenceNewRequest;
import cz.adamzrcek.modules.preference.service.PreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;
    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @Operation(summary = "Get preference by id", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<PreferenceDto> getPreference(@PathVariable Long id){
        return ResponseEntity.ok(preferenceService.getPreference(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PreferenceDto>> listPreferences(){
        return ResponseEntity.ok(preferenceService.getAllPreferencesForSignedUser());
    }

    @GetMapping("/list/{category}")
    public ResponseEntity<List<PreferenceDto>> listPreferencesByCategory(@PathVariable Long category){
        return ResponseEntity.ok(preferenceService.getAllPreferencesForSignedUserByCategory(category));
    }

    @GetMapping("/list/connection")
    public ResponseEntity<List<PreferenceDto>> listPreferencesForConnection(){
        return ResponseEntity.ok(preferenceService.getPartnerPreferences());
    }

    @GetMapping("/list/connection/{category}")
    public ResponseEntity<List<PreferenceDto>> listPreferencesForConnectionByCategory(@PathVariable Long category) {
        return ResponseEntity.ok(preferenceService.getPartnerPreferencesByCategory(category));
    }

    @PostMapping
    public ResponseEntity<PreferenceDto> createPreference(@RequestBody PreferenceNewRequest request){
        return ResponseEntity.ok(preferenceService.createPreference(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreferenceDto> updatePreference(@PathVariable Long id, @RequestBody PreferenceNewRequest request){
        return ResponseEntity.ok(preferenceService.updatePreference(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long id){
        preferenceService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }

}
