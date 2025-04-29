package cz.adamzrcek.modules.user.repository;

import cz.adamzrcek.modules.privacy.annotation.LogRepository;
import cz.adamzrcek.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@LogRepository(entityType = "user", excludedMethods = {"count", "existsById"})
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameIgnoreCase(String username);
}
