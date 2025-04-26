package cz.adamzrcek.service;

import cz.adamzrcek.dtos.ImportantDateCategoryDto;
import cz.adamzrcek.entity.ImportantDateCategory;
import cz.adamzrcek.repository.ImportantDateCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ImportantDateCategoryService {
    private final ImportantDateCategoryRepository importantDateCategoryRepository;

    @Cacheable("importantDateCategories")
    public List<ImportantDateCategoryDto> getAllCategories(){
        return importantDateCategoryRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private ImportantDateCategoryDto convertToDto(ImportantDateCategory category){
        return new ImportantDateCategoryDto(category.getId(), category.getName());
    }

}
