package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.referencedata.dtos.ImportantDateCategoryDto;

import java.util.List;

public interface ImportantDateCategoryService {
    List<ImportantDateCategoryDto> getAllCategories();
}
