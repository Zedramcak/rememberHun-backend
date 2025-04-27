package cz.adamzrcek.service;

import cz.adamzrcek.dtos.ImportantDateCategoryDto;

import java.util.List;

public interface ImportantDateCategoryService {
    List<ImportantDateCategoryDto> getAllCategories();
}
