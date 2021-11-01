package ru.gb.antonov.j710.order.dtos;

import static ru.gb.antonov.j710.monolith.Factory.STR_EMPTY;

/** Считаем, что юзер может заполнять адрес так, как ему заблагорассудиться. Если он что-то сделает
 направильно, то менеждер поможет ему исправить это при телефонном разговоре. Если юзер безнадёжен
 настолько, что неправильно указал номер телефона, то упаваем на то, что он не единственный наш клиент.
 А деньги мы ему вернём. */
public class ShippingDetailesDto
{
    private String postcode;
    private String country;
    private String region;
    private String town;
    private String strit;
    private String building;
    private String apartment;
    private String deliveryType;

    public ShippingDetailesDto() {}

    public String getPostcode ()     { return postcode; }
    public String getCountry ()      { return country; }
    public String getRegion ()       { return region; }
    public String getTown ()         { return town; }
    public String getBuilding ()     { return building; }
    public String getApartment ()    { return apartment; }
    public String getStrit ()        { return strit; }
    public String getDeliveryType () { return deliveryType; }

    public void setPostcode (String value)     { postcode     = set (value); }
    public void setCountry (String value)      { country      = set (value); }
    public void setRegion (String value)       { region       = set (value); }
    public void setTown (String value)         { town         = set (value); }
    public void setStrit (String value)        { strit        = set (value); }
    public void setBuilding (String value)     { building     = set (value); }
    public void setApartment (String value)    { apartment    = set (value); }
    public void setDeliveryType (String value) { deliveryType = set (value); }

    private static String set (String value) { return (value == null) ? STR_EMPTY : value; }
}//1