package ru.gb.antonov.j710.productreview.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductReviewDto;
import ru.gb.antonov.j710.productreview.services.ProductReviewService;

import java.security.Principal;
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
System.out.println("\n****************** ProductReviewController.loadProductReviews << /load_reviews/{"+pid+"} *******************\n");
        if (pid == null)
        {
System.err.println("Не могу выполнить поиск для товара id: " + pid);
            throw new BadCreationParameterException ("Не могу выполнить поиск для товара id: " + pid);
        }
        List<ProductReviewDto> result = productReviewService.getReviewListById (pid);
System.out.println("ProductReviewController.loadProductReviews >> "+ result);
        return result;
    }

    @PostMapping ("/new_review")
    public void newProductReview (@RequestBody ProductReviewDto reviewDto, Principal principal)
    {
        productReviewService.newProductReview (reviewDto.getProductId(), reviewDto.getText(), principal);
    }

    @GetMapping ("/can_review/{pid}")
    public Boolean canReviews (@PathVariable Long pid, Principal principal)
    {
        return productReviewService.canReview (principal, pid);
    }
}
