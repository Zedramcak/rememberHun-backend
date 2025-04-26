package cz.adamzrcek.repository;

import cz.adamzrcek.entity.WishlistCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistCategoryRepository extends JpaRepository<WishlistCategory, Long> {
    WishlistCategory findByName(String name);
}
