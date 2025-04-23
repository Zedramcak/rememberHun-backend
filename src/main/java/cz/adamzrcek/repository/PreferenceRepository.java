package cz.adamzrcek.repository;

import cz.adamzrcek.entity.Preference;
import cz.adamzrcek.entity.User;
import cz.adamzrcek.entity.enums.PreferencesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    List<Preference> findAllByUser(User user);
    List<Preference> findAllByUserAndCategory(User user, PreferencesCategory category);
}
