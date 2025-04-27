package cz.adamzrcek.service.impl;

import cz.adamzrcek.dtos.PreferenceCategoryDto;
import cz.adamzrcek.entity.PreferenceCategory;
import cz.adamzrcek.repository.PreferenceCategoryRepository;
import cz.adamzrcek.service.PreferenceCategoryService;
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
