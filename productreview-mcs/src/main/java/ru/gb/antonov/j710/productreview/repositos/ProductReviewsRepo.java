package ru.gb.antonov.j710.productreview.repositos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.productreview.entities.ProductReview;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ProductReviewsRepo extends CrudRepository<ProductReview, Long> {

    Collection<ProductReview> findAllByProductId (Long id);
    Optional<ProductReview> findByProductIdAndOuruserId (Long pid, Long uid/*OurUser u*/);
}
