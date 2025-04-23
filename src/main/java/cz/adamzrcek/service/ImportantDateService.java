package cz.adamzrcek.service;

import cz.adamzrcek.dtos.connection.ConnectionSignedUserResponse;
import cz.adamzrcek.dtos.importantDate.ImportantDateDto;
import cz.adamzrcek.dtos.importantDate.ImportantDateRequest;
import cz.adamzrcek.entity.Connection;
import cz.adamzrcek.entity.ImportantDate;
import cz.adamzrcek.exception.NotAllowedException;
import cz.adamzrcek.repository.ConnectionRepository;
import cz.adamzrcek.repository.ImportantDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportantDateService {
    private final ImportantDateRepository importantDateRepository;
    private final ConnectionService connectionService;
    private final ConnectionRepository connectionRepository;

    public ImportantDateDto createImportantDate(ImportantDateRequest request) {
        Connection connection = getCurrentConnectionEntity();

        ImportantDate importantDate = ImportantDate.builder()
                .title(request.title())
                .note(request.note())
                .date(request.date())
                .type(request.type())
                .shouldBeNotified(request.shouldBeNotified())
                .connection(connection)
                .build();

        return toDto(importantDateRepository.save(importantDate));
    }

    public List<ImportantDateDto> getAllImportantDates() {
        return importantDateRepository.findAllByConnection(getCurrentConnectionEntity())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public ImportantDateDto getImportantDate(Long id) {
        ImportantDate date = findByIdAndValidateAccess(id);
        return toDto(date);
    }

    public ImportantDateDto updateImportantDate(Long id, ImportantDateRequest request) {
        ImportantDate date = findByIdAndValidateAccess(id);

        date.setTitle(request.title());
        date.setNote(request.note());
        date.setDate(request.date());
        date.setType(request.type());
        date.setShouldBeNotified(request.shouldBeNotified());

        return toDto(importantDateRepository.save(date));
    }

    public void deleteImportantDate(Long id) {
        ImportantDate date = findByIdAndValidateAccess(id);
        importantDateRepository.delete(date);
    }

    // 🔒 Helpers

    private Connection getCurrentConnectionEntity() {
        ConnectionSignedUserResponse connectionDto = connectionService.getCurrentConnection();
        if (connectionDto == null) {
            throw new NotAllowedException("User is not connected to anyone");
        }

        return connectionRepository.findById(connectionDto.id())
                .orElseThrow(() -> new IllegalArgumentException("Connection with id " + connectionDto.id() + " not found"));
    }

    private ImportantDate findByIdAndValidateAccess(Long id) {
        var connection = getCurrentConnectionEntity();

        ImportantDate date = importantDateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Important date with id " + id + " not found"));

        if (!date.getConnection().equals(connection)) {
            throw new NotAllowedException("User is not allowed to access this important date");
        }

        return date;
    }

    private ImportantDateDto toDto(ImportantDate importantDate) {
        return new ImportantDateDto(
                importantDate.getId(),
                importantDate.getTitle(),
                importantDate.getNote(),
                importantDate.getDate(),
                importantDate.getType(),
                importantDate.isShouldBeNotified()
        );
    }
}
