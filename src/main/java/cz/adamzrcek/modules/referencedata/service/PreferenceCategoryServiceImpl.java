package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.referencedata.dtos.PreferenceCategoryDto;
import cz.adamzrcek.modules.referencedata.entity.PreferenceCategory;
import cz.adamzrcek.modules.referencedata.repository.PreferenceCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PreferenceCategoryServiceImpl implements PreferenceCategoryService {
    private final PreferenceCategoryRepository preferenceCategoryRepository;

    @Cacheable("preferenceCategories")
    public List<PreferenceCategoryDto> getAllCategories(){
        return preferenceCategoryRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private PreferenceCategoryDto convertToDto(PreferenceCategory category){
        return new PreferenceCategoryDto(category.getId(), category.getName());
    }
}
