package ru.gb.antonov.j710.monolith.entities.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static ru.gb.antonov.j710.monolith.Factory.STR_EMPTY;

public class ProductReviewDto
{
    private String authorName;
    private String text;
    private String date;
    private Long   productId;
//----------------------------------------------------------
    protected ProductReviewDto(){}
    public ProductReviewDto (Long uid/*String name*/, String txt, LocalDateTime ldt, Long pid)
    {
        if (uid != null/*sayNoToEmptyStrings (name, txt)*/ && pid != null)
        {
            authorName = uid.toString()/*name*/;    //TODO: пофиксить это упрощение
            text       = txt;
            date       = (ldt != null) ? ldt.format (DateTimeFormatter.ofLocalizedDate (FormatStyle.SHORT))
                                       : STR_EMPTY;
            productId = pid;
        }
    }

    public String getAuthorName ()                { return authorName; }
    public void setAuthorName (String authorName) { this.authorName = authorName; }

    public String getText ()          { return text; }
    public void setText (String text) { this.text = text; }

    public String getDate ()          { return date; }
    public void setDate (String date) { this.date = date; }

    public Long getProductId ()                   { return productId; }
    public void setProductId (Long productId)     { this.productId = productId; }
}
