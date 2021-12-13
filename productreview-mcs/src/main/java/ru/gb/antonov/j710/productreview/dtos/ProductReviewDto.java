package ru.gb.antonov.j710.productreview.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static ru.gb.antonov.j710.monolith.Factory.NO_STRING;
import static ru.gb.antonov.j710.monolith.Factory.STR_EMPTY;

public class ProductReviewDto {

    private Long   userId;  //< введено при «отпиливании», тк ProductReview утратил возможность быстрого преобразования id в login
    private String authorName;
    private String text;
    private String date;
    private Long   productId;
//----------------------------------------------------------
    protected ProductReviewDto(){}
    public ProductReviewDto (Long uid, String name, String txt, LocalDateTime ldt, Long pid) {

        if (pid != null  &&  txt != null  &&  !txt.isBlank())
        {
            userId     = uid;
            authorName = (name != NO_STRING) ? name : STR_EMPTY;
            text       = txt;
            date       = (ldt != null) ? ldt.format (DateTimeFormatter.ofLocalizedDate (FormatStyle.SHORT))
                                       : STR_EMPTY;
            productId  = pid;
        }
    }

    public Long getUserId ()           { return userId; }
    public void setUserId (Long value) { userId = value; }

    public String getAuthorName ()           { return authorName; }
    public void setAuthorName (String value) { this.authorName = value; }

    public String getText ()          { return text; }
    public void setText (String text) { this.text = text; }

    public String getDate ()          { return date; }
    public void setDate (String date) { this.date = date; }

    public Long getProductId ()           { return productId; }
    public void setProductId (Long value) { this.productId = value; }
}
