package cz.adamzrcek.modules.privacy.repository;

import cz.adamzrcek.modules.privacy.entity.DataAccessLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataAccessLogsRepository extends JpaRepository<DataAccessLogs, Long> {
}
