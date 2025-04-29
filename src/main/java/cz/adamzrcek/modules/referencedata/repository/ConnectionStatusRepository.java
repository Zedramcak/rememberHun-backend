package cz.adamzrcek.modules.referencedata.repository;

import cz.adamzrcek.modules.referencedata.entity.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionStatusRepository extends JpaRepository<ConnectionStatus, Long> {
    ConnectionStatus findByStatus(String status);
}
