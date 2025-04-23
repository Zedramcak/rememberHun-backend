package cz.adamzrcek.dtos.wishlistItem;

import cz.adamzrcek.entity.enums.WishlistCategory;

import java.time.LocalDateTime;

public record WishlistItemDto (
    Long id,
    String title,
    String description,
    WishlistCategory category,
    boolean fulfilled,
    LocalDateTime createdAt){}
