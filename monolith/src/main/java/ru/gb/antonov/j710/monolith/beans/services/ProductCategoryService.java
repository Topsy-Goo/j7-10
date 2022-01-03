package ru.gb.antonov.j710.monolith.beans.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.ResourceNotFoundException;
import ru.gb.antonov.j710.monolith.beans.repositos.ProductCategoryRepo;
import ru.gb.antonov.j710.monolith.entities.ProductsCategory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepo productCategoryRepo;

/** @throws ResourceNotFoundException */
    public ProductsCategory findByName (String name) {

        String errMsg = "Товарная категория не найдена: " + name;
        return productCategoryRepo.findByName (name)
                                  .orElseThrow (()->new ResourceNotFoundException (errMsg));
    }

    public List<String> getCategoriesList () {
        return productCategoryRepo.findAllNames();
    }
}
