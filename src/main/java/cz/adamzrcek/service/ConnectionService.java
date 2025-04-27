package cz.adamzrcek.service;

import cz.adamzrcek.dtos.connection.ConnectionAcceptRequest;
import cz.adamzrcek.dtos.connection.ConnectionDeleteRequest;
import cz.adamzrcek.dtos.connection.ConnectionDto;
import cz.adamzrcek.dtos.connection.ConnectionNewRequest;
import cz.adamzrcek.dtos.connection.ConnectionSignedUserResponse;

public interface ConnectionService {
    ConnectionDto createNewConnection(ConnectionNewRequest request);
    ConnectionDto acceptConnection(ConnectionAcceptRequest request);
    void deleteConnection(ConnectionDeleteRequest request);
    ConnectionSignedUserResponse getCurrentConnection();
}
