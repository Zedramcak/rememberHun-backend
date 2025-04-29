package cz.adamzrcek.modules.importantdate.repository;

import cz.adamzrcek.modules.connection.entity.Connection;
import cz.adamzrcek.modules.importantdate.entity.ImportantDate;
import cz.adamzrcek.modules.privacy.annotation.LogRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@LogRepository(entityType = "importantDate")
public interface ImportantDateRepository extends JpaRepository<ImportantDate, Long> {
    List<ImportantDate> findAllByConnection(Connection connection);
}
