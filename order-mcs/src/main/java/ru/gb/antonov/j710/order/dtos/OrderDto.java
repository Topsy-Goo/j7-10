package ru.gb.antonov.j710.order.dtos;

import ru.gb.antonov.j710.monolith.entities.dtos.OrderItemDto;

import java.util.List;

public class OrderDto
{
    private Long   orderNumber;
    private String address;
    private String phone;
    private double cost;    //< общая стоимость выбранных/купленных товаров
    private List<OrderItemDto> oitems;
    private int    load;    //< общее количество единиц выбранных/купленных товаров
    private String state;

    public OrderDto(){}

    public String getAddress ()             { return address; }
    public void setAddress (String address) { this.address = address; }

    public String getPhone ()               { return phone; }
    public void setPhone (String phone)     { this.phone = phone; }

    public double getCost ()                { return cost; }
    public void setCost (double cost)       { this.cost = cost; }

    public Long getOrderNumber ()                     { return orderNumber; }
    public void setOrderNumber (Long orderNumber)     { this.orderNumber = orderNumber; }

    public List<OrderItemDto> getOitems ()            { return oitems; }
    public void setOitems (List<OrderItemDto> oitems) { this.oitems = oitems; }

    public int getLoad ()               { return load; }
    public void setLoad (int load)      { this.load = load; }

    public String getState ()           { return state; }
    public void setState (String state) { this.state = state; }
}
