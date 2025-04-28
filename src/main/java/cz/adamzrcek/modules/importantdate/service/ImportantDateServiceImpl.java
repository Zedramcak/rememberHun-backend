package cz.adamzrcek.modules.importantdate.service;

import cz.adamzrcek.modules.connection.dtos.ConnectionSignedUserResponse;
import cz.adamzrcek.modules.importantdate.dtos.ImportantDateDto;
import cz.adamzrcek.modules.importantdate.dtos.ImportantDateRequest;
import cz.adamzrcek.modules.connection.entity.Connection;
import cz.adamzrcek.modules.importantdate.entity.ImportantDate;
import cz.adamzrcek.modules.auth.exception.NotAllowedException;
import cz.adamzrcek.modules.shared.exception.ResourceNotFoundException;
import cz.adamzrcek.modules.connection.repository.ConnectionRepository;
import cz.adamzrcek.modules.referencedata.repository.ImportantDateCategoryRepository;
import cz.adamzrcek.modules.importantdate.repository.ImportantDateRepository;
import cz.adamzrcek.modules.connection.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportantDateServiceImpl implements ImportantDateService {
    private final ImportantDateRepository importantDateRepository;
    private final ImportantDateCategoryRepository importantDateCategoryRepository;
    private final ConnectionService connectionService;
    private final ConnectionRepository connectionRepository;

    public ImportantDateDto createImportantDate(ImportantDateRequest request) {
        Connection connection = getCurrentConnectionEntity();

        ImportantDate importantDate = ImportantDate.builder()
                .title(request.title())
                .note(request.note())
                .date(request.date())
                .category(importantDateCategoryRepository.findById(request.typeId()).orElseThrow(() -> new ResourceNotFoundException("Important date category with id " + request.typeId() + " not found")))
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
        date.setCategory(importantDateCategoryRepository.findById(request.typeId())
                .orElseThrow(() -> new ResourceNotFoundException("Important date category with id " + request.typeId() + " not found")));
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
                importantDate.getCategory().getName(),
                importantDate.isShouldBeNotified()
        );
    }
}
