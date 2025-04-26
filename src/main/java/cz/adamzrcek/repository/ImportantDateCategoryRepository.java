package cz.adamzrcek.repository;

import cz.adamzrcek.entity.ImportantDateCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportantDateCategoryRepository extends JpaRepository<ImportantDateCategory, Long> {
    ImportantDateCategory findByName(String name);
}
