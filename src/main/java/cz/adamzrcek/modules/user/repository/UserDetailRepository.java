package cz.adamzrcek.modules.user.repository;

import cz.adamzrcek.modules.user.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
}