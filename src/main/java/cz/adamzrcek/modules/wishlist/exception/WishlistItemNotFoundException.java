package cz.adamzrcek.modules.wishlist.exception;

import cz.adamzrcek.exception.ResourceNotFoundException;

public class WishlistItemNotFoundException extends ResourceNotFoundException {
    public WishlistItemNotFoundException(String message) {
        super(message);
    }
}
