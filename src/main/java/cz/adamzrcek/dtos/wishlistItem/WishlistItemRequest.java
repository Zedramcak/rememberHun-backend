package cz.adamzrcek.dtos.wishlistItem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WishlistItemRequest {
    private String title;
    private String description;
    private Long categoryId;
}
