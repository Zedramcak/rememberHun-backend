package cz.adamzrcek.repository;

import cz.adamzrcek.entity.User;
import cz.adamzrcek.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findAllByUser(User user);
}
