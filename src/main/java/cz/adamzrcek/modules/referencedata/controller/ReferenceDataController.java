package cz.adamzrcek.modules.referencedata.controller;

import cz.adamzrcek.modules.shared.dtos.ConnectionStatusDto;
import cz.adamzrcek.modules.referencedata.dtos.ImportantDateCategoryDto;
import cz.adamzrcek.modules.referencedata.dtos.PreferenceCategoryDto;
import cz.adamzrcek.modules.referencedata.dtos.RoleDto;
import cz.adamzrcek.modules.referencedata.dtos.WishlistCategoryDto;
import cz.adamzrcek.modules.referencedata.service.ConnectionStatusService;
import cz.adamzrcek.modules.referencedata.service.ImportantDateCategoryService;
import cz.adamzrcek.modules.referencedata.service.PreferenceCategoryService;
import cz.adamzrcek.modules.referencedata.service.RoleService;
import cz.adamzrcek.modules.referencedata.service.WishlistCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reference-data")
@AllArgsConstructor
public class ReferenceDataController {

    private final ImportantDateCategoryService importantDateCategoryService;
    private final ConnectionStatusService connectionStatusService;
    private final WishlistCategoryService wishlistCategoryService;
    private final PreferenceCategoryService preferenceCategoryService;
    private final RoleService roleService;

    @Operation(summary = "List all date categories", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @GetMapping("/important-date-categories")
    public ResponseEntity<List<ImportantDateCategoryDto>> listImportantDateCategories(){
        List<ImportantDateCategoryDto> response = importantDateCategoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all connection statuses", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @GetMapping("/connection-statuses")
    public ResponseEntity<List<ConnectionStatusDto>> listConnectionStatuses(){
        List<ConnectionStatusDto> response = connectionStatusService.getAllStatuses();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all wishlist categories", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @GetMapping("/wishlist-categories")
    public ResponseEntity<List<WishlistCategoryDto>> listWishlistCategories(){
        List<WishlistCategoryDto> response = wishlistCategoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all preference categories", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @GetMapping("/preference-categories")
    public ResponseEntity<List<PreferenceCategoryDto>> listPreferenceCategories(){
        List<PreferenceCategoryDto> response = preferenceCategoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all roles", responses = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }


}
