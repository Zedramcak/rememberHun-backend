package cz.adamzrcek.service;

import cz.adamzrcek.dtos.ConnectionStatusDto;
import cz.adamzrcek.dtos.connection.ConnectionAcceptRequest;
import cz.adamzrcek.dtos.connection.ConnectionDeleteRequest;
import cz.adamzrcek.dtos.connection.ConnectionDto;
import cz.adamzrcek.dtos.connection.ConnectionNewRequest;
import cz.adamzrcek.dtos.connection.ConnectionSignedUserResponse;
import cz.adamzrcek.dtos.user.UserDto;
import cz.adamzrcek.entity.Connection;
import cz.adamzrcek.entity.ConnectionStatus;
import cz.adamzrcek.entity.User;
import cz.adamzrcek.exception.ConnectionNotFoundException;
import cz.adamzrcek.exception.NotAllowedException;
import cz.adamzrcek.repository.ConnectionRepository;
import cz.adamzrcek.repository.ConnectionStatusRepository;
import cz.adamzrcek.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final ConnectionStatusRepository connectionStatusRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ConnectionDto createNewConnection(ConnectionNewRequest request) {
        User currentUser = userService.getCurrentUser();

        if (!connectionRepository.findActiveConnectionsForUser(currentUser).isEmpty()) {
            throw new NotAllowedException("Current user already has an active connection");
        }

        log.debug("User {} is creating new connection.", currentUser.getUsername());

        User userToConnect = userService.getUserById(request.getUserId());

        if (userToConnect.equals(currentUser)) {
            throw new NotAllowedException("Can't connect to yourself");
        }

        if (!connectionRepository.findActiveConnectionsForUser(userToConnect).isEmpty()) {
            throw new NotAllowedException("User " + userToConnect.getUsername() + " already has an active connection");
        }

        Connection newConnection = Connection.builder()
                .user1(currentUser)
                .user2(userToConnect)
                .connectionStatus(connectionStatusRepository.findByStatus("PENDING"))
                .build();

        log.debug("New connection created for users {} and {}", currentUser.getUsername(), userToConnect.getUsername());

        return toDto(connectionRepository.save(newConnection));
    }

    private ConnectionDto toDto(Connection connection){
        return new ConnectionDto(
                connection.getId(),
                toUserDto(connection.getUser1()),
                toUserDto(connection.getUser2()),
                connection.getCreated_at(),
                connection.getConnectionStatus().getStatus()
        );
    }

    private UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, null);
    }

    @Transactional
    public ConnectionDto acceptConnection(ConnectionAcceptRequest request) {
        User currentUser = userService.getCurrentUser();

        Connection connection = connectionRepository.findById(request.getConnectionId())
                .orElseThrow(() -> new ConnectionNotFoundException("Connection not found"));

        if (!connection.getUser2().equals(currentUser)) {
            throw new NotAllowedException("User is not allowed to accept this connection");
        }

        connection.setConnectionStatus(connectionStatusRepository.findByStatus("CONNECTED"));

        log.debug("User {} accepted connection with id {}", currentUser.getUsername(), connection.getId());

        currentUser.setConnection(connection);

        User userToConnect = connection.getUser1();
        userToConnect.setConnection(connection);

        userRepository.save(userToConnect);
        userRepository.save(currentUser);

        log.debug("User {} and {} are connected", currentUser.getUsername(), userToConnect.getUsername());

        return toDto(connectionRepository.save(connection));
    }

    @Transactional
    public void deleteConnection(ConnectionDeleteRequest request) {
        Connection connection = connectionRepository.findById(request.getConnectionId())
                .orElseThrow(() -> new ConnectionNotFoundException("Connection with id " + request.getConnectionId() + " not found"));

        User currentUser = userService.getCurrentUser();

        if (!connection.getUser1().equals(currentUser) && !connection.getUser2().equals(currentUser)) {
            throw new NotAllowedException("User is not allowed to delete this connection");
        }


        log.debug("User {} requested deletion connection with id {}", currentUser.getUsername(), connection.getId());

        connection.setConnectionStatus(connectionStatusRepository.findByStatus("DELETED"));

        currentUser.setConnection(null);
        userRepository.save(currentUser);

        User secondUser = connection.getUser1().equals(currentUser) ? connection.getUser2() : connection.getUser1();
        secondUser.setConnection(null);
        userRepository.save(secondUser);

        log.debug("User {} and {} are disconnected", currentUser.getUsername(), secondUser.getUsername());

        connectionRepository.save(connection);

        log.debug("User {} deleted connection with id {}", currentUser.getUsername(), connection.getId());
    }

    public ConnectionSignedUserResponse getCurrentConnection() {
        User currentUser = userService.getCurrentUser();
        log.debug("User {} requested current connection", currentUser.getUsername());
        if (currentUser.getConnection() == null) {
            return null;
        }

        Connection connection = currentUser.getConnection();
        log.debug("User {} has current connection with id {}", currentUser.getUsername(), connection.getId());
        return new ConnectionSignedUserResponse(
                connection.getId(),
                toUserDto(connection.getUser1().equals(currentUser) ? connection.getUser2() : connection.getUser1()),
                connection.getCreated_at(),
                connection.getConnectionStatus().getStatus());
    }

    public List<ConnectionStatusDto> getAllConnectionsStatusesAsMap() {
        List<ConnectionStatus> statuses = connectionStatusRepository.findAll();
        return statuses.stream().map(status -> new ConnectionStatusDto(status.getId(), status.getStatus())).toList();
    }
}
