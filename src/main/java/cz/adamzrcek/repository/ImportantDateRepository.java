package cz.adamzrcek.repository;

import cz.adamzrcek.entity.Connection;
import cz.adamzrcek.entity.ImportantDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportantDateRepository extends JpaRepository<ImportantDate, Long> {
    List<ImportantDate> findAllByConnection(Connection connection);
}
