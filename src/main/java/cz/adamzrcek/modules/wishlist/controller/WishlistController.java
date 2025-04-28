package cz.adamzrcek.modules.wishlist.controller;

import cz.adamzrcek.modules.wishlist.dtos.WishlistItemDto;
import cz.adamzrcek.modules.wishlist.dtos.WishlistItemRequest;
import cz.adamzrcek.modules.wishlist.service.WishlistService;
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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Operation(summary = "Create a wishlist item", responses = {
            @ApiResponse(responseCode = "201", description = "Wishlist item created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
    })
    @PostMapping
    public ResponseEntity<WishlistItemDto> createWishlistItem(@RequestBody WishlistItemRequest request) {
        WishlistItemDto createdItem = wishlistService.createWishlistItem(request);

        URI location = URI.create(String.format("/api/v1/wishlist/%d", createdItem.id()));
        return ResponseEntity.created(location).body(createdItem);
    }

    @GetMapping()
    public ResponseEntity<List<WishlistItemDto>> getOwnWishlist() {
        return ResponseEntity.ok(wishlistService.getOwnWishlist());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishlistItemDto> getWishlistItem(@PathVariable Long id) {
        return ResponseEntity.ok(wishlistService.getWishlistItem(id));
    }

    @GetMapping("/partner")
    public ResponseEntity<List<WishlistItemDto>> getPartnerWishlist() {
        return ResponseEntity.ok(wishlistService.getPartnerWishlist());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WishlistItemDto> updateWishlistItem(@PathVariable Long id, @RequestBody WishlistItemRequest request) {
        return ResponseEntity.ok(wishlistService.updateWishlistItem(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlistItem(@PathVariable Long id) {
        wishlistService.deleteWishlistItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/toggle-fulfilled")
    public ResponseEntity<WishlistItemDto> toggleFulfilled(@PathVariable Long id) {
        return ResponseEntity.ok(wishlistService.toggleFulfilledWishlistItem(id));
    }
}
