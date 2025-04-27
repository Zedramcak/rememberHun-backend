package cz.adamzrcek.service;

import cz.adamzrcek.dtos.ConnectionStatusDto;

import java.util.List;

public interface ConnectionStatusService {
    List<ConnectionStatusDto> getAllStatuses();
}
