package cz.adamzrcek.modules.referencedata.repository;

import cz.adamzrcek.modules.referencedata.entity.ImportantDateCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportantDateCategoryRepository extends JpaRepository<ImportantDateCategory, Long> {
    ImportantDateCategory findByName(String name);
}
