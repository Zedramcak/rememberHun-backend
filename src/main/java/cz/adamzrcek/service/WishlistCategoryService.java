package cz.adamzrcek.service;

import cz.adamzrcek.dtos.WishlistCategoryDto;
import cz.adamzrcek.entity.WishlistCategory;
import cz.adamzrcek.repository.WishlistCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WishlistCategoryService {
    private final WishlistCategoryRepository wishlistCategoryRepository;

    @Cacheable("wishlistCategories")
    public List<WishlistCategoryDto> getAllCategories(){
        return wishlistCategoryRepository.findAll().stream().map(this::convertToDto).toList();
    }

    private WishlistCategoryDto convertToDto(WishlistCategory category){
        return new WishlistCategoryDto(category.getId(), category.getName());
    }
}
