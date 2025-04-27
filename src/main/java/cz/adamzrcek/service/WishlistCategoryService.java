package cz.adamzrcek.service;

import cz.adamzrcek.dtos.WishlistCategoryDto;

import java.util.List;

public interface WishlistCategoryService {
    List<WishlistCategoryDto> getAllCategories();
}
