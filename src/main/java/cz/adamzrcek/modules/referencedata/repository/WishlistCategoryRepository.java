package cz.adamzrcek.modules.referencedata.repository;

import cz.adamzrcek.modules.referencedata.entity.WishlistCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistCategoryRepository extends JpaRepository<WishlistCategory, Long> {
    WishlistCategory findByName(String name);
}
