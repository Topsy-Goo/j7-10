package ru.gb.antonov.j710.productreview.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductReviewDto;
import ru.gb.antonov.j710.productreview.services.ProductReviewService;

import java.util.List;

@RestController
@RequestMapping ("/api/v1/productreviews")
@RequiredArgsConstructor
@CrossOrigin ("*")
public class ProductReviewController
{
    private final ProductReviewService productReviewService;

    @GetMapping ("/load_reviews/{id}")
    public List<ProductReviewDto> loadProductReviews (@PathVariable (name="id") Long pid)
    {
        if (pid == null)
            throw new BadCreationParameterException ("Не могу выполнить поиск для товара id: " + pid);

        return productReviewService.getReviewListById (pid);
    }

    @PostMapping ("/new_review")
    public void newProductReview (@RequestBody ProductReviewDto reviewDto, @RequestHeader String username)
    {
        productReviewService.newProductReview (reviewDto.getProductId(), reviewDto.getText(), username);
    }

    @GetMapping ("/can_review/{pid}")
    public Boolean canReviews (@PathVariable Long pid, @RequestHeader String username)
    {
        return productReviewService.canReview (username, pid);
    }
}
