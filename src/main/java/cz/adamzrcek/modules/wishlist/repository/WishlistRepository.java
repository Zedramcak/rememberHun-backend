package cz.adamzrcek.modules.wishlist.repository;

import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.wishlist.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findAllByUser(User user);
}
