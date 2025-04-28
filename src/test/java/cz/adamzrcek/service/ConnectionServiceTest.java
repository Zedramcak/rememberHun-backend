package cz.adamzrcek.service;

import cz.adamzrcek.modules.connection.dtos.ConnectionAcceptRequest;
import cz.adamzrcek.modules.connection.dtos.ConnectionDeleteRequest;
import cz.adamzrcek.modules.connection.dtos.ConnectionNewRequest;
import cz.adamzrcek.modules.connection.entity.Connection;
import cz.adamzrcek.entity.ConnectionStatus;
import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.user.entity.UserDetail;
import cz.adamzrcek.modules.connection.exception.ConnectionNotFoundException;
import cz.adamzrcek.modules.auth.exception.NotAllowedException;
import cz.adamzrcek.modules.user.service.UserService;
import cz.adamzrcek.repository.ConnectionRepository;
import cz.adamzrcek.repository.ConnectionStatusRepository;
import cz.adamzrcek.modules.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {
    @InjectMocks
    private ConnectionService connectionService;

    @Mock
    private UserService userService;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    ConnectionStatusRepository connectionStatusRepository;


    @Test
    public void createConnectionOkTest(){
        var request = new ConnectionNewRequest(1L);
        var loggedInUser = User.builder().id(2L).username("username1").userDetail(UserDetail.builder().connection(null).build()).build();
        var userToConnect = User.builder().id(1L).username("username2").userDetail(UserDetail.builder().connection(null).build()).build();
        var connection = Connection.builder()
                .id(1L)
                .user1(loggedInUser)
                .user2(userToConnect)
                .connectionStatus(new ConnectionStatus(1L, "USER"))
                .created_at(LocalDateTime.MAX)
                .build();

        when(userService.getCurrentUser()).thenReturn(loggedInUser);
        when(connectionRepository.findActiveConnectionsForUser(any(User.class))).thenReturn(List.of());
        when(userService.getUserById(any(Long.class))).thenReturn(userToConnect);
        when(connectionRepository.save(any(Connection.class))).thenReturn(connection);
        given(connectionStatusRepository.findByStatus(anyString())).willReturn(new ConnectionStatus(1L, "PENDING"));

        var result = connectionService.createNewConnection(request);

        assertEquals(connection.getId(), result.id());
        assertEquals(connection.getUser1().getId(), result.user1().id());
        assertEquals(connection.getUser2().getId(), result.user2().id());
        assertEquals(connection.getCreated_at(), result.created_at());
        assertEquals(connection.getConnectionStatus().getStatus(), result.status());

        verify(connectionRepository, times(2)).findActiveConnectionsForUser(any(User.class));
        verify(userService, times(1)).getUserById(any(Long.class));
        verify(connectionRepository, times(1)).save(any(Connection.class));
    }

    @Test
    public void createConnectionFailCurrentUserHasConnectionTest(){
        var request = new ConnectionNewRequest(1L);
        var loggedInUser = User.builder().id(2L).username("username1").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();

        when(userService.getCurrentUser()).thenReturn(loggedInUser);
        when(connectionRepository.findActiveConnectionsForUser(loggedInUser)).thenReturn(List.of(Connection.builder().build()));

        assertThrows(NotAllowedException.class, () -> connectionService.createNewConnection(request));

        verify(connectionRepository, times(1)).findActiveConnectionsForUser(any(User.class));
        verify(userService, never()).getUserById(any(Long.class));
        verify(connectionRepository, never()).save(any(Connection.class));
    }

    @Test
    public void createConnectionUserToConnectHasConnectionTest(){
        var request = new ConnectionNewRequest(1L);
        var loggedInUser = User.builder().id(2L).username("username1").userDetail(UserDetail.builder().connection(null).build()).build();
        var userToConnect = User.builder().id(1L).username("username2").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();

        when(userService.getCurrentUser()).thenReturn(loggedInUser);
        when(connectionRepository.findActiveConnectionsForUser(loggedInUser)).thenReturn(List.of());
        when(userService.getUserById(1L)).thenReturn(userToConnect);
        when(connectionRepository.findActiveConnectionsForUser(userToConnect)).thenReturn(List.of(Connection.builder().build()));

        assertThrows(NotAllowedException.class, () -> connectionService.createNewConnection(request));

        verify(connectionRepository, times(2)).findActiveConnectionsForUser(any(User.class));
        verify(userService, times(1)).getUserById(any(Long.class));
        verify(connectionRepository, never()).save(any(Connection.class));
    }

    @Test
    public void createConnectionUserToConnectIsCurrentUserTest(){
        var request = new ConnectionNewRequest(1L);
        var loggedInUser = User.builder().id(1L).username("username1").userDetail(UserDetail.builder().connection(Connection.builder().id(null).build()).build()).build();

        when(userService.getCurrentUser()).thenReturn(loggedInUser);
        when(connectionRepository.findActiveConnectionsForUser(loggedInUser)).thenReturn(List.of());
        when(userService.getUserById(1L)).thenReturn(loggedInUser);

        assertThrows(NotAllowedException.class, () -> connectionService.createNewConnection(request));

        verify(connectionRepository, times(1)).findActiveConnectionsForUser(any(User.class));
        verify(userService, times(1)).getUserById(any(Long.class));
        verify(connectionRepository, never()).save(any(Connection.class));
    }

    @Test
    public void acceptConnectionOkTest(){
        var request = new ConnectionAcceptRequest(1L);
        var loggedInUser = User.builder().id(2L).username("username1").userDetail(UserDetail.builder().connection(Connection.builder().id(null).build()).build()).build();
        var userToConnect = User.builder().id(1L).username("username2").userDetail(UserDetail.builder().connection(Connection.builder().id(null).build()).build()).build();
        var connectionToAccept = Connection.builder()
                .id(1L)
                .user1(userToConnect)
                .user2(loggedInUser)
                .connectionStatus(new ConnectionStatus(1L, "PENDING")).build();
        var connectionToSave = Connection.builder()
                .id(1L)
                .user1(userToConnect)
                .user2(loggedInUser)
                .connectionStatus(new ConnectionStatus(1L, "CONNECTED")).build();


        when(userService.getCurrentUser()).thenReturn(loggedInUser);
        when(connectionRepository.findById(request.getConnectionId())).thenReturn(Optional.of(connectionToAccept));
        when(userRepository.save(any())).thenReturn(null);
        when(connectionRepository.save(any())).thenReturn(connectionToSave);
        given(connectionStatusRepository.findByStatus(anyString())).willReturn(new ConnectionStatus(1L, "CONNECTED"));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Connection> connectionArgumentCaptor = ArgumentCaptor.forClass(Connection.class);

        var result = connectionService.acceptConnection(request);

        verify(connectionRepository, times(1)).findById(request.getConnectionId());
        verify(connectionRepository, times(1)).save(connectionArgumentCaptor.capture());
        verify(userRepository, times(2)).save(userArgumentCaptor.capture());

        var capturedUsers = userArgumentCaptor.getAllValues();
        var capturedConnections = connectionArgumentCaptor.getAllValues();

        assertEquals(2, capturedUsers.size());
        assertEquals(1, capturedConnections.size());

        assertEquals(result.id(), capturedConnections.getFirst().getId());
        assertEquals(result.user1().id(), capturedUsers.getFirst().getId());
        assertEquals(result.user2().id(), capturedUsers.getLast().getId());
        assertEquals(result.status(), capturedConnections.getFirst().getConnectionStatus().getStatus());

    }

    @Test
    public void deleteConnectionOkTest() {
        var request = new ConnectionDeleteRequest(1L);
        var loggedInUser = User.builder().id(2L).username("username1").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();
        var userToConnect = User.builder().id(1L).username("username2").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();
        var connectionToDelete = Connection.builder().id(1L).user1(userToConnect).user2(loggedInUser).connectionStatus(new ConnectionStatus(1L, "CONNECTED")).build();

        when(userService.getCurrentUser()).thenReturn(loggedInUser);
        when(connectionRepository.findById(request.getConnectionId())).thenReturn(Optional.of(connectionToDelete));
        when(userRepository.save(any())).thenReturn(null);
        when(connectionRepository.save(any())).thenReturn(connectionToDelete);

        connectionService.deleteConnection(request);

        verify(connectionRepository, times(1)).findById(request.getConnectionId());
        verify(userRepository, times(2)).save(any(User.class));
        verify(connectionRepository, times(1)).save(any(Connection.class));
    }

    @Test
    public void deleteConnectionNotFoundTest() {
        var request = new ConnectionDeleteRequest(1L);

        when(connectionRepository.findById(request.getConnectionId())).thenReturn(Optional.empty());

        assertThrows(ConnectionNotFoundException.class, () -> connectionService.deleteConnection(request));

        verify(connectionRepository, times(1)).findById(request.getConnectionId());
        verify(userRepository, never()).save(any(User.class));
        verify(connectionRepository, never()).save(any(Connection.class));
    }

    @Test
    public void deleteConnectionNotAllowedTest() {
        var request = new ConnectionDeleteRequest(1L);
        var loggedInUser = User.builder().id(2L).username("username1").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();
        var userToConnect1 = User.builder().id(1L).username("username2").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();
        var userToConnect2 = User.builder().id(3L).username("username3").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();
        var connectionToDelete = Connection.builder().id(1L).user1(userToConnect1).user2(userToConnect2).connectionStatus(new ConnectionStatus(1L, "CONNECTED")).build();

        when(userService.getCurrentUser()).thenReturn(loggedInUser);
        when(connectionRepository.findById(request.getConnectionId())).thenReturn(Optional.of(connectionToDelete));

        assertThrows(NotAllowedException.class, () -> connectionService.deleteConnection(request));

        verify(connectionRepository, times(1)).findById(request.getConnectionId());
        verify(userRepository, never()).save(any(User.class));
        verify(connectionRepository, never()).save(any(Connection.class));
    }

    @Test
    public void getCurrentConnectionTest() {
        var currentUser = User.builder().id(2L).username("username1").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();
        var partnerUser = User.builder().id(1L).username("username2").userDetail(UserDetail.builder().connection(Connection.builder().id(1L).build()).build()).build();
        var connection = Connection.builder()
                .id(1L)
                .user1(currentUser)
                .user2(partnerUser)
                .connectionStatus(new ConnectionStatus(1L, "PENDING"))
                .created_at(LocalDateTime.MAX)
                .build();

        currentUser.setUserDetail(UserDetail.builder().connection(connection).build());

        when(userService.getCurrentUser()).thenReturn(currentUser);

        var result = connectionService.getCurrentConnection();

        assertNotNull(result);
        assertEquals(connection.getId(), result.id());
        assertEquals(connection.getUser2().getId(), result.partner().id());
        assertEquals(connection.getCreated_at(), result.created_at());
        assertEquals(connection.getConnectionStatus().getStatus(), result.status());
    }

    @Test
    public void getCurrentConnectionNoCurrentConnectionTest() {
        var currentUser = User.builder().id(2L).username("username1").userDetail(UserDetail.builder().connection(Connection.builder().id(null).build()).build()).build();

        when(userService.getCurrentUser()).thenReturn(currentUser);

        var result = connectionService.getCurrentConnection();

        assertNull(result);
    }
}
