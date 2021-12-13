package ru.gb.antonov.j710.order.dtos;

import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;

public class OrderDetalesDto {
    private Long            orderNumber;
    private String          orderState;
    private String          orderCreationTime;
    private ShippingInfoDto shippingInfoDto;
    private CartDto         cartDto;
//-------------------------------------------------------------------------------------
    public OrderDetalesDto (){}
//-------------------------------------------------------------------------------------
    public CartDto getCartDto ()            { return cartDto; }
    public void setCartDto (CartDto value)  { cartDto = value; }

    public Long getOrderNumber ()           { return orderNumber; }
    public void setOrderNumber (Long value) { orderNumber = value; }

    public String getOrderCreationTime ()           { return orderCreationTime; }
    public void setOrderCreationTime (String value) { orderCreationTime = value; }

    public String getOrderState ()                  { return orderState; }
    public void setOrderState (String value)        { orderState = value; }

    public ShippingInfoDto getShippingInfoDto ()           { return shippingInfoDto; }
    public void setShippingInfoDto (ShippingInfoDto value) { shippingInfoDto = value; }
}//1