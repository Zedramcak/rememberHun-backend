package cz.adamzrcek.service;

import cz.adamzrcek.modules.shared.dtos.ConnectionStatusDto;

import java.util.List;

public interface ConnectionStatusService {
    List<ConnectionStatusDto> getAllStatuses();
}
