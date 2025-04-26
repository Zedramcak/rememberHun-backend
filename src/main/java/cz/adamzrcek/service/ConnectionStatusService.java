package cz.adamzrcek.service;

import cz.adamzrcek.dtos.ConnectionStatusDto;
import cz.adamzrcek.entity.ConnectionStatus;
import cz.adamzrcek.repository.ConnectionStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConnectionStatusService {
    private final ConnectionStatusRepository connectionStatusRepository;

    @Cacheable("connectionStatuses")
    public List<ConnectionStatusDto> getAllStatuses(){
        return connectionStatusRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private ConnectionStatusDto convertToDto(ConnectionStatus status){
        return new ConnectionStatusDto(status.getId(), status.getStatus());
    }
}
