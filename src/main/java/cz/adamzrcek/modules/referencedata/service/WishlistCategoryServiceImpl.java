package cz.adamzrcek.modules.referencedata.service;

import cz.adamzrcek.modules.referencedata.dtos.WishlistCategoryDto;
import cz.adamzrcek.modules.referencedata.entity.WishlistCategory;
import cz.adamzrcek.modules.referencedata.repository.WishlistCategoryRepository;
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
