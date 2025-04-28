package cz.adamzrcek.modules.preference.repository;

import cz.adamzrcek.modules.preference.entity.Preference;
import cz.adamzrcek.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    List<Preference> findAllByUser(User user);

    List<Preference> findAllByUserAndCategory_Id(User user, Long categoryId);
}
