package cz.adamzrcek.service.impl;

import cz.adamzrcek.dtos.WishlistCategoryDto;
import cz.adamzrcek.entity.WishlistCategory;
import cz.adamzrcek.repository.WishlistCategoryRepository;
import cz.adamzrcek.service.WishlistCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WishlistCategoryServiceImpl implements WishlistCategoryService {
    private final WishlistCategoryRepository wishlistCategoryRepository;

    @Cacheable("wishlistCategories")
    @Override
    public List<WishlistCategoryDto> getAllCategories(){
        return wishlistCategoryRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private WishlistCategoryDto convertToDto(WishlistCategory category){
        return new WishlistCategoryDto(category.getId(), category.getName());
    }
}
