package cz.adamzrcek.modules.referencedata.repository;

import cz.adamzrcek.modules.referencedata.entity.PreferenceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceCategoryRepository extends JpaRepository<PreferenceCategory, Long> {
    PreferenceCategory findByName(String name);
}
