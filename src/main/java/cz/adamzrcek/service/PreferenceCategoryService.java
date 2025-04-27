package cz.adamzrcek.service;

import cz.adamzrcek.dtos.PreferenceCategoryDto;

import java.util.List;

public interface PreferenceCategoryService {
    List<PreferenceCategoryDto> getAllCategories();
}
