package ru.gb.antonov.j710.monolith.entities.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

import static ru.gb.antonov.j710.monolith.Factory.PROD_TITLELEN_MAX;
import static ru.gb.antonov.j710.monolith.Factory.PROD_TITLELEN_MIN;

public class ProductDto {

    private Long productId;

    @NotNull (message="Не задано название товара!")
    @Length (min= PROD_TITLELEN_MIN, max= PROD_TITLELEN_MAX, message="Длина названия товара: 3…255 символов!")
    private String title;

    private BigDecimal price = BigDecimal.ZERO;

    @PositiveOrZero (message="Остаток товара должен быть НЕОТРИЦАТЕЛЬНЫМ числом!")
    private int rest;

    @NotNull (message="\rНе указана еденица измерения!")
    private String measure;

    @NotNull (message="Не указано название категории товара!")
    private String category;
//--------------------------------------------------------------
    public ProductDto (){}

    public ProductDto (Long pProductId, String pTitle, BigDecimal pPrice, Integer pRest,
                       String pMeasure, String pCategory)
    {
       if (pProductId != null) this.productId = pProductId;
       if (pTitle     != null) this.title     = pTitle;
       if (pPrice     != null) this.price     = pPrice;
       if (pRest      != null) this.rest      = pRest;
       if (pMeasure   != null) this.measure   = pMeasure;
       if (pCategory  != null) this.category  = pCategory;
    }
//--------------------------------------------------------------
    public Long getProductId ()           { return productId; }
    public void setProductId (Long value) { productId = value; }

    public String getTitle ()             { return title; }
    public void setTitle (String value)   { title = value; }

    public BigDecimal getPrice ()           { return price; }
    public void setPrice (BigDecimal value) { price = value; }

    public int getRest ()                   { return rest; }
    public void setRest (int value)         { rest = value; }

    public String getMeasure ()             { return measure; }
    public void   setMeasure (String value) { measure = value; }

    public String getCategory ()            { return category; }
    public void setCategory (String value)  { category = value; }
//--------------------------------------------------------------

    @Override public String toString() {
        return String.format ("ProductDto:{pid:%d, title:«%s», price:%f, rest:%d, msr:«%s», categ:«%s»}",
                                         productId, title,     price,    rest,    measure,  category);
    }
}
