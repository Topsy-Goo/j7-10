package ru.gb.antonov.j710.monolith.beans.repositos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.monolith.entities.ProductsCategory;

import java.util.Optional;

@Repository
public interface ProductCategoryRepo extends JpaRepository<ProductsCategory, Integer> {

    Optional<ProductsCategory> findByName (String name);
}
