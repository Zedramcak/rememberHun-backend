package cz.adamzrcek.modules.importantdate.service;

import cz.adamzrcek.modules.importantdate.dtos.ImportantDateDto;
import cz.adamzrcek.modules.importantdate.dtos.ImportantDateRequest;

import java.util.List;

public interface ImportantDateService {
    ImportantDateDto createImportantDate(ImportantDateRequest request);
    List<ImportantDateDto> getAllImportantDates();
    ImportantDateDto getImportantDate(Long id);
    ImportantDateDto updateImportantDate(Long id, ImportantDateRequest request);
    void deleteImportantDate(Long id);
}
