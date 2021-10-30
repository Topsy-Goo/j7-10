package ru.gb.antonov.j710.order.dtos;

import org.hibernate.validator.constraints.Length;
import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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

    private Long       orderNumber;
    private BigDecimal deliveryCost = BigDecimal.ZERO;
    private String     orderState;
    private BigDecimal overallCost = BigDecimal.ZERO;
    private String     orderCreationTime;
    //private ShippingDetailesDto shippingDetailesDto;

/*  @Length (max=255, message="Максимальная длина текста комментария — 255 символов.")
    private String comment;*/
//-------------------------------------------------------------------------------------
    public OrderDetalesDto (){}
//-------------------------------------------------------------------------------------

    public CartDto getCartDto ()            { return cartDto; }
    public void setCartDto (CartDto value)  { cartDto = value; }

    public String getPhone ()               { return phone; }
    public void setPhone (String value)     { phone = value; }

    public String getAddress ()             { return address; }
    public void setAddress (String value)   { address = value; }

    public Long getOrderNumber ()           { return orderNumber; }
    public void setOrderNumber (Long value) { orderNumber = value; }

    public String getOrderCreationTime ()           { return orderCreationTime; }
    public void setOrderCreationTime (String value) { orderCreationTime = value; }

    public BigDecimal getDeliveryCost ()           { return deliveryCost; }
    public void setDeliveryCost (BigDecimal value) { deliveryCost = value; }

    public String getOrderState ()             { return orderState; }
    public void setOrderState (String value)   { orderState = value; }

    public BigDecimal getOverallCost ()            { return overallCost; }
    public void setOverallCost (BigDecimal value)  { overallCost = value; }

    //public ShippingDetailesDto getShippingDetailesDto ()           { return shippingDetailesDto; }
    //public void setShippingDetailesDto (ShippingDetailesDto value) { shippingDetailesDto = value; }
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
        oddto.deliveryCost = BigDecimal.ZERO;
        oddto.orderState   = STR_EMPTY;
        oddto.overallCost  = BigDecimal.ZERO;
        //oddto. = ;
        return oddto;
    }
}
