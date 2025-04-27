package cz.adamzrcek.service.impl;

import cz.adamzrcek.dtos.wishlistItem.WishlistItemDto;
import cz.adamzrcek.dtos.wishlistItem.WishlistItemRequest;
import cz.adamzrcek.entity.User;
import cz.adamzrcek.entity.WishlistItem;
import cz.adamzrcek.exception.NotAllowedException;
import cz.adamzrcek.repository.WishlistCategoryRepository;
import cz.adamzrcek.repository.WishlistRepository;
import cz.adamzrcek.service.ConnectionService;
import cz.adamzrcek.service.UserService;
import cz.adamzrcek.service.WishlistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserService userService;
    private final ConnectionService connectionService;
    private final WishlistCategoryRepository wishlistCategoryRepository;

    @Override
    public WishlistItemDto getWishlistItem(Long id) {
        return convertToDto(findOwnWishlistItemOrThrow(id));
    }

    @Override
    public WishlistItemDto createWishlistItem(WishlistItemRequest request){
        var currentUser = userService.getCurrentUser();
        WishlistItem wishlistItem = toWishlistItem(request, currentUser);

        return convertToDto(wishlistRepository.save(wishlistItem));
    }

    @Override
    public List<WishlistItemDto> getOwnWishlist() {
        return getWishlistByUser(userService.getCurrentUser());
    }

    @Override
    public List<WishlistItemDto> getPartnerWishlist() {
        var connection = connectionService.getCurrentConnection();
        if (connection == null) {
            throw new NotAllowedException("User is not connected to anyone");
        }
        User partner = userService.getUserById(connection.partner().id());
        return getWishlistByUser(partner);
    }

    @Override
    public void deleteWishlistItem(Long id){
        WishlistItem wishlistItem = findOwnWishlistItemOrThrow(id);

        wishlistRepository.delete(wishlistItem);
    }

    @Override
    public WishlistItemDto updateWishlistItem(Long id, WishlistItemRequest request){
        WishlistItem wishlistItem = findOwnWishlistItemOrThrow(id);
        updateWishlistItemFields(request, wishlistItem);

        return convertToDto(wishlistRepository.save(wishlistItem));
    }

    @Override
    public WishlistItemDto toggleFulfilledWishlistItem(Long id){

        WishlistItem wishlistItem = findOwnWishlistItemOrThrow(id);

        wishlistItem.setFulfilled(!wishlistItem.isFulfilled());

        return convertToDto(wishlistRepository.save(wishlistItem));
    }

    private WishlistItem toWishlistItem(WishlistItemRequest request, User user) {
        return WishlistItem.builder()
                .title(request.getTitle())
                .user(user)
                .category(
                        wishlistCategoryRepository.findById(request.getCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Wishlist category with id " + request.getCategoryId() + " not found")))
                .description(request.getDescription())
                .build();
    }

    private void updateWishlistItemFields(WishlistItemRequest request, WishlistItem wishlistItem) {
        wishlistItem.setTitle(request.getTitle());
        wishlistItem.setDescription(request.getDescription());
        wishlistItem.setCategory(wishlistCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Wishlist category with id " + request.getCategoryId() + " not found")));
    }

    private List<WishlistItemDto> getWishlistByUser(User user){
        return wishlistRepository.findAllByUser(user).stream().map(this::convertToDto).toList();
    }

    private WishlistItem findOwnWishlistItemOrThrow(Long id){
        var currentUser = userService.getCurrentUser();
        WishlistItem wishlistItem = wishlistRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Wishlist item with id " + id + " not found"));
        if (!wishlistItem.getUser().equals(currentUser)) {
            throw new NotAllowedException("User is not allowed to access this wishlist item");
        }
        return wishlistItem;
    }

    private WishlistItemDto convertToDto(WishlistItem wishlistItem) {
        return new WishlistItemDto(wishlistItem.getId(), wishlistItem.getTitle(), wishlistItem.getDescription(), wishlistItem.getCategory().getName(), wishlistItem.isFulfilled(), wishlistItem.getCreatedAt());
    }
}
