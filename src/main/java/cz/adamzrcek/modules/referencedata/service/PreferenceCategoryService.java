package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.referencedata.dtos.PreferenceCategoryDto;

import java.util.List;

public interface PreferenceCategoryService {
    List<PreferenceCategoryDto> getAllCategories();
}
