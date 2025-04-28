package cz.adamzrcek.service;

import cz.adamzrcek.modules.connection.dtos.ConnectionAcceptRequest;
import cz.adamzrcek.modules.connection.dtos.ConnectionDeleteRequest;
import cz.adamzrcek.modules.connection.dtos.ConnectionDto;
import cz.adamzrcek.modules.connection.dtos.ConnectionNewRequest;
import cz.adamzrcek.modules.connection.dtos.ConnectionSignedUserResponse;

public interface ConnectionService {
    ConnectionDto createNewConnection(ConnectionNewRequest request);
    ConnectionDto acceptConnection(ConnectionAcceptRequest request);
    void deleteConnection(ConnectionDeleteRequest request);
    ConnectionSignedUserResponse getCurrentConnection();
}
