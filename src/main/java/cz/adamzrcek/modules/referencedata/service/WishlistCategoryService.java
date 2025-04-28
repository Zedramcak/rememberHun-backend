package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.referencedata.dtos.WishlistCategoryDto;

import java.util.List;

public interface WishlistCategoryService {
    List<WishlistCategoryDto> getAllCategories();
}
