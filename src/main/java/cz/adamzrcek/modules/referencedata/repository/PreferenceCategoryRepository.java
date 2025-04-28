package cz.adamzrcek.modules.referencedata.repository;

import cz.adamzrcek.modules.referencedata.entity.PreferenceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceCategoryRepository extends JpaRepository<PreferenceCategory, Long> {
    PreferenceCategory findByName(String name);
}
