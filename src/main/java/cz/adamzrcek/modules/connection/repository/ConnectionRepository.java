package cz.adamzrcek.modules.connection.repository;

import cz.adamzrcek.modules.connection.entity.Connection;
import cz.adamzrcek.modules.privacy.annotation.LogRepository;
import cz.adamzrcek.modules.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@LogRepository(entityType = "connection")
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    @Query("SELECT c FROM Connection c WHERE (c.user1 = :user OR c.user2 = :user) AND c.connectionStatus.status <> 'DELETED'")
    List<Connection> findActiveConnectionsForUser(@Param("user") User user);
}
