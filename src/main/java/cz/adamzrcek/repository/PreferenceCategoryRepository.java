package cz.adamzrcek.repository;

import cz.adamzrcek.entity.PreferenceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceCategoryRepository extends JpaRepository<PreferenceCategory, Long> {
    PreferenceCategory findByName(String name);
}
