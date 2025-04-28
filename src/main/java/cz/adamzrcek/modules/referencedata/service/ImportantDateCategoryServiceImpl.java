package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.referencedata.dtos.ImportantDateCategoryDto;
import cz.adamzrcek.modules.referencedata.entity.ImportantDateCategory;
import cz.adamzrcek.modules.referencedata.repository.ImportantDateCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ImportantDateCategoryServiceImpl implements ImportantDateCategoryService {
    private final ImportantDateCategoryRepository importantDateCategoryRepository;

    @Cacheable("importantDateCategories")
    @Override
    public List<ImportantDateCategoryDto> getAllCategories(){
        return importantDateCategoryRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private ImportantDateCategoryDto convertToDto(ImportantDateCategory category){
        return new ImportantDateCategoryDto(category.getId(), category.getName());
    }

}
