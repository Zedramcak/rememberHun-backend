package cz.adamzrcek.service;

import cz.adamzrcek.dtos.wishlistItem.WishlistItemDto;
import cz.adamzrcek.dtos.wishlistItem.WishlistItemRequest;

import java.util.List;

public interface WishlistService {
    WishlistItemDto getWishlistItem(Long id);
    WishlistItemDto createWishlistItem(WishlistItemRequest request);
    List<WishlistItemDto> getOwnWishlist();
    List<WishlistItemDto> getPartnerWishlist();
    void deleteWishlistItem(Long id);
    WishlistItemDto updateWishlistItem(Long id, WishlistItemRequest request);
    WishlistItemDto toggleFulfilledWishlistItem(Long id);
}
