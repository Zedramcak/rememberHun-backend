package cz.adamzrcek.modules.referencedata.repository;

import cz.adamzrcek.modules.referencedata.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
