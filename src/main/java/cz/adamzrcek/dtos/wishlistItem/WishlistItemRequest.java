package cz.adamzrcek.dtos.wishlistItem;

import cz.adamzrcek.entity.enums.WishlistCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WishlistItemRequest {
    private String title;
    private String description;
    private WishlistCategory category;
}
