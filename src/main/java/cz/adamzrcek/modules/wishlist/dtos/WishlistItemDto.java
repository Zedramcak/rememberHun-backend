package cz.adamzrcek.modules.wishlist.dtos;

import java.time.LocalDateTime;

public record WishlistItemDto (
    Long id,
    String title,
    String description,
    String category,
    boolean fulfilled,
    LocalDateTime createdAt){}
