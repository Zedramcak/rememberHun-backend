package cz.adamzrcek.modules.referencedata.repository;

import cz.adamzrcek.modules.referencedata.entity.WishlistCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistCategoryRepository extends JpaRepository<WishlistCategory, Long> {
    WishlistCategory findByName(String name);
}
