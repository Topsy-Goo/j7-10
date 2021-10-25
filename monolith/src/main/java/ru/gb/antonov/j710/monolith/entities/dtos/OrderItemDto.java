package ru.gb.antonov.j710.monolith.entities.dtos;

public class OrderItemDto
{
    private Long productId;
    private String category;
    private String title;
    private double price;
    private int rest;
    private int quantity;
    private double cost;
//------------------------------------------------------------------
    public OrderItemDto (Long productId, String category, String title, Double price,
                         Integer rest, Integer quantity, Double cost)
    {
        if (productId != null) this.productId = productId;
        if (category  != null) this.category  = category;
        if (title     != null) this.title     = title;
        if (price     != null) this.price     = price;
        if (rest      != null) this.rest      = rest;
        if (quantity  != null) this.quantity  = quantity;
        if (cost      != null) this.cost      = cost;
    }
/*    public OrderItemDto (OrderItemDto oi) //TODO: проверить, используется ли этот метод ?
    {
        productId = oi.productId;
        title     = oi.title;
        category  = oi.category;
        price     = oi.price;
        rest      = oi.rest;
        quantity  = oi.quantity;
        cost      = oi.cost;
    }*/
    public OrderItemDto () {}
//--------- Геттеры и сеттеры (JSON работает с публичными полями!) --------------

    //Возвращает true, если количество было изменено.
    public boolean setQuantity (int newQuantity)
    {
        boolean ok = newQuantity >= 0 && quantity != newQuantity;
        if (ok)
            quantity = newQuantity;
        return ok;
    }
    public int getQuantity ()         { return quantity; }

    public double getCost ()          { return price * quantity; }
    public void setCost (double cost) { this.cost = cost; }

    public Long getProductId ()               { return productId; }
    public void setProductId (Long productId) { this.productId = productId; }

    public String getCategory ()              { return category; }
    public void setCategory (String category) { this.category = category; }

    public String getTitle ()           { return title; }
    public void setTitle (String title) { this.title = title; }

    public double getPrice ()           { return price; }
    public void setPrice (double price) { this.price = price; }

    public int getRest ()               { return rest; }
    public void setRest (int rest)      { this.rest = rest; }

//----------------- Другие методы ----------------------------------

    //Возвращает true, если количество было изменено.
    public boolean changeQuantity (int delta) {   return setQuantity (quantity + delta);   }

    //private void calcCost () {   setCost (price * quantity);   }
}
