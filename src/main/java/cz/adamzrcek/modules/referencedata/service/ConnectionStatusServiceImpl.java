package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.shared.dtos.ConnectionStatusDto;
import cz.adamzrcek.modules.referencedata.entity.ConnectionStatus;
import cz.adamzrcek.modules.referencedata.repository.ConnectionStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ConnectionStatusServiceImpl implements ConnectionStatusService {
    private final ConnectionStatusRepository connectionStatusRepository;

    @Cacheable("connectionStatuses")
    @Override
    public List<ConnectionStatusDto> getAllStatuses(){
        return connectionStatusRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private ConnectionStatusDto convertToDto(ConnectionStatus status){
        return new ConnectionStatusDto(status.getId(), status.getStatus());
    }
}
