package cz.adamzrcek.modules.wishlist.exception;

import cz.adamzrcek.modules.shared.exception.ResourceNotFoundException;

public class WishlistItemNotFoundException extends ResourceNotFoundException {
    public WishlistItemNotFoundException(String message) {
        super(message);
    }
}
