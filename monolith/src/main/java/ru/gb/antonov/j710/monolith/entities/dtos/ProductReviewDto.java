package ru.gb.antonov.j710.monolith.entities.dtos;

import lombok.Data;
import ru.gb.antonov.j710.monolith.entities.ProductReview;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Data
public class ProductReviewDto
{
    private String authorName;
    private String text;
    private String date;
    private Long productId;
//----------------------------------------------------------
    protected ProductReviewDto(){}
    public ProductReviewDto (ProductReview review)
    {
        if (review != null)
        {
            authorName = review.getOurUser().getLogin();
            text = review.getText();
            date = review.getCreatedAt().toLocalDate().format (DateTimeFormatter.ofLocalizedDate (FormatStyle.SHORT));
            productId = review.getProductId();
        }
    }
}
