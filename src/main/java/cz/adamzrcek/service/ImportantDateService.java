package cz.adamzrcek.service;

import cz.adamzrcek.dtos.importantDate.ImportantDateDto;
import cz.adamzrcek.dtos.importantDate.ImportantDateRequest;

import java.util.List;

public interface ImportantDateService {
    ImportantDateDto createImportantDate(ImportantDateRequest request);
    List<ImportantDateDto> getAllImportantDates();
    ImportantDateDto getImportantDate(Long id);
    ImportantDateDto updateImportantDate(Long id, ImportantDateRequest request);
    void deleteImportantDate(Long id);
}
