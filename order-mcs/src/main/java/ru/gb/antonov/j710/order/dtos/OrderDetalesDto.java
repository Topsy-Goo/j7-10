package ru.gb.antonov.j710.order.dtos;

import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.gb.antonov.j710.monolith.Factory.STR_EMPTY;
import static ru.gb.antonov.j710.monolith.Factory.orderCreationTimeToString;

public class OrderDetalesDto
{
    private Long            orderNumber;
    private String          orderState;
    private String          orderCreationTime;
    private ShippingInfoDto shippingInfoDto;

    @NotNull (message="\rПолучена пустая корзина.\rЗаказ не может быть оформлен.")
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
//-------------------------------------------------------------------------------------

    public static OrderDetalesDto dummyOrderDetalesDto (ShippingInfoDto sidto)
    {
        OrderDetalesDto oddto = new OrderDetalesDto();
        LocalDateTime   ldt   = LocalDateTime.now();
        oddto.cartDto           = CartDto.dummyCartDto();
        oddto.shippingInfoDto   = sidto;
        oddto.orderNumber       = 0L;
        oddto.orderCreationTime = orderCreationTimeToString(ldt);
        oddto.orderState        = STR_EMPTY;
        return oddto;
    }
}
