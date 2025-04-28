package cz.adamzrcek.modules.referencedata.repository;

import cz.adamzrcek.modules.referencedata.entity.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionStatusRepository extends JpaRepository<ConnectionStatus, Long> {
    ConnectionStatus findByStatus(String status);
}
