package ru.gb.antonov.j710.monolith.entities.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.gb.antonov.j710.monolith.Factory.*;

public class OrderDetalesDto
{
    @NotNull (message="\rПолучена пустая корзина.\rЗаказ не может быть оформлен.")
    private CartDto cartDto;

    @NotNull (message="\rУкажите номер телефона.")
    @Length (min=DELIVERING_PHONE_LEN_MIN, max=DELIVERING_PHONE_LEN_MAX, message="\rНомер телефона должен содержать 10…16 цифр.\rПример: 8006004050.")//TODO: поправить длину номера.
    private String phone;

    @NotNull (message="\rУкажите адрес доставки.")
    @Length (max=DELIVERING_ADDRESS_LEN_MAX, message="\rМаксимальная длина адреса — 255 символов.")
    private String address;

    private Long   orderNumber;
//  private Long   orderCreatedAt;
    private String orderCreationTime;
//  private String deliveryType;
    private double deliveryCost;
    private String orderState;
    private double overallCost;

/*  @Length (max=255, message="Максимальная длина текста комментария — 255 символов.")
    private String comment;*/
//-------------------------------------------------------------------------------------
    public OrderDetalesDto (){}
//-------------------------------------------------------------------------------------

    public CartDto getCartDto ()                  { return cartDto; }
    public void setCartDto (CartDto cartDto)      { this.cartDto = cartDto; }

    public String getPhone ()                     { return phone; }
    public void setPhone (String phone)           { this.phone = phone; }

    public String getAddress ()                   { return address; }
    public void setAddress (String address)       { this.address = address; }

    public Long getOrderNumber ()                 { return orderNumber; }
    public void setOrderNumber (Long orderNumber) { this.orderNumber = orderNumber; }

    public String getOrderCreationTime ()                       { return orderCreationTime; }
    public void setOrderCreationTime (String orderCreationTime) { this.orderCreationTime = orderCreationTime; }

    public double getDeliveryCost ()                  { return deliveryCost; }
    public void setDeliveryCost (double deliveryCost) { this.deliveryCost = deliveryCost; }

    public String getOrderState ()                    { return orderState; }
    public void setOrderState (String orderState)     { this.orderState = orderState; }

    public double getOverallCost ()                   { return overallCost; }
    public void setOverallCost (double overallCost)   { this.overallCost = overallCost; }

//-------------------------------------------------------------------------------------
    public static OrderDetalesDto dummyOrderDetalesDto (String phone, String address)
    {
        OrderDetalesDto oddto = new OrderDetalesDto();
        LocalDateTime ldt = LocalDateTime.now();
        oddto.cartDto     = CartDto.dummyCartDto();
        oddto.phone       = phone;
        oddto.address     = address;
        oddto.orderNumber = 0L;
        //oddto.orderCreatedAt = ldt.getLong (ChronoField.MILLI_OF_SECOND;
        oddto.orderCreationTime = orderCreationTimeToString(ldt);
        //oddto.deliveryType = STR_EMPTY;
        oddto.deliveryCost = 0.0;
        oddto.orderState   = STR_EMPTY;
        oddto.overallCost  = 0.0;
        //oddto. = ;
        return oddto;
    }
}
