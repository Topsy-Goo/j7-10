package ru.gb.antonov.j710.monolith.entities.dtos;

import java.math.BigDecimal;

public class OrderItemDto {

    private Long       productId;
    private String     title;
    private BigDecimal price = BigDecimal.ZERO;
    private int        quantity;
    private String     measure;     //< единицы измерения есть OrderItemDto, но отсутствуют в OrderItem
    private int        rest;
    private String     category;
    private BigDecimal cost = BigDecimal.ZERO;
//------------------------------------------------------------------
    public OrderItemDto () {}

/** Метод меняет только те поля, для которых был передан не null. */
    public OrderItemDto (Long productId, String category, String title, BigDecimal price,
                         Integer rest, Integer quantity, BigDecimal cost, String measure)
    {
        if (productId != null) this.productId = productId;
        if (category  != null) this.category  = category;
        if (title     != null) this.title     = title;
        if (price     != null) this.price     = price;
        if (rest      != null) this.rest      = rest;
        if (quantity  != null) this.quantity  = quantity;
        if (cost      != null) this.cost      = cost;
        if (measure   != null) this.measure   = measure;
    }
//--------- Геттеры и сеттеры (JSON работает с публичными полями!) --------------

    //Возвращает true, если количество было изменено.
    public boolean setQuantity (int newQuantity) {
        boolean ok = newQuantity >= 0 && quantity != newQuantity;
        if (ok)
            quantity = newQuantity;
        return ok;
    }
    public int getQuantity ()         { return quantity; }

    public BigDecimal getCost ()           { return price.multiply (new BigDecimal(quantity)); }
    public void setCost (BigDecimal value) { cost = value; }

    public Long getProductId ()            { return productId; }
    public void setProductId (Long value)  { productId = value; }

    public String getCategory ()           { return category; }
    public void setCategory (String value) { category = value; }

    public String getTitle ()           { return title; }
    public void setTitle (String value) { title = value; }

    public BigDecimal getPrice ()           { return price; }
    public void setPrice (BigDecimal value) { price = value; }

    public int getRest ()           { return rest; }
    public void setRest (int value) { rest = value; }

    public String getMeasure ()           { return measure; }
    public void setMeasure (String value) { measure = value; }

//----------------- Другие методы ----------------------------------

/** Возвращает true, если количество было изменено.    */
    public boolean changeQuantity (int delta) {   return setQuantity (quantity + delta);   }
}
