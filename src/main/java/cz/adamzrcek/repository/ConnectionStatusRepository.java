package cz.adamzrcek.repository;

import cz.adamzrcek.entity.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionStatusRepository extends JpaRepository<ConnectionStatus, Long> {
    ConnectionStatus findByStatus(String status);
}
