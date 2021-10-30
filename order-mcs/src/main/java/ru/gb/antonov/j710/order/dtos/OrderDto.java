package ru.gb.antonov.j710.order.dtos;

import ru.gb.antonov.j710.monolith.entities.dtos.OrderItemDto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDto
{
    private Long               orderNumber;
    private String             address;
    private String             phone;
    private BigDecimal         cost = BigDecimal.ZERO;    //< общая стоимость выбранных/купленных товаров
    private List<OrderItemDto> oitems;
    private int                load;    //< общее количество единиц выбранных/купленных товаров
    private String             state;

    public OrderDto(){}

    public String getAddress ()             { return address; }
    public void setAddress (String value)   { this.address = value; }

    public String getPhone ()               { return phone; }
    public void setPhone (String value)     { this.phone = value; }

    public BigDecimal getCost ()            { return cost; }
    public void setCost (BigDecimal value)  { this.cost = value; }

    public Long getOrderNumber ()           { return orderNumber; }
    public void setOrderNumber (Long value) { this.orderNumber = value; }

    public List<OrderItemDto> getOitems ()           { return oitems; }
    public void setOitems (List<OrderItemDto> value) { this.oitems = value; }

    public int getLoad ()               { return load; }
    public void setLoad (int value)     { this.load = value; }

    public String getState ()           { return state; }
    public void setState (String value) { this.state = value; }
}
