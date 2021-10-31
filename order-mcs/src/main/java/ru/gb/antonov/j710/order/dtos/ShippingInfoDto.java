package ru.gb.antonov.j710.order.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import static ru.gb.antonov.j710.monolith.Factory.*;

@Data    @NoArgsConstructor
public class ShippingInfoDto
{
    private Long id;

    //@NotNull (message="Код страны, 2 латинских символа.")
    //@Length (min=DELIVERING_COUNTRYCODE_LEN_MIN, max=DELIVERING_COUNTRYCODE_LEN_MAX, message="Код страны, 2 латинских символа.")
    private String countryCode = "RU";

    //@NotNull (message="Укажите почтовый индекс (6 цифр).")
    //@Length (min=DELIVERING_POSTALCODE_LEN_MIN, max=DELIVERING_POSTALCODE_LEN_MAX, message="Почтовый индекс, 6 цифр.")
    private String postalCode;

    //@NotNull (message="Укажите свой субъект федерации, если есть (область, республику, край, автономный округ).")
    //@Length (min=DELIVERING_REGION_LEN_MIN, max=DELIVERING_REGION_LEN_MAX)
    private String region;

    //@NotNull (message="Укажите населённый пункт.")
    //@Length (min=DELIVERING_TOWN_VILLAGE_LEN_MIN, max=DELIVERING_TOWN_VILLAGE_LEN_MAX, message="Название населённого пункта, до 100 символов.")
    private String townVillage;

    //@NotNull (message="Укажите улицу, номер дома, корпус/строение, если есть.")
    //@Length (min=DELIVERING_STREET_HOUSE_LEN_MIN, max=DELIVERING_STREET_HOUSE_LEN_MAX, message="Улица и номер дома, до 100 символов.")
    private String streetHouse;

    //@NotNull (message="\rУкажите номер квартиры/офиса или укажите «нет».")
    //@Length (min=DELIVERING_APARTMENT_LEN_MIN, max=DELIVERING_APARTMENT_LEN_MAX)
    private String apartment;

    @NotNull (message="\rУкажите номер телефона.")
    @Length (min=DELIVERING_PHONE_LEN_MIN, max=DELIVERING_PHONE_LEN_MAX, message="\rНомер телефона, 10…20 символов.\rПример: 8006004050 или +7(800) 600-40-50.")
    private String phone;
//-------------------------------------------------------------------------

    @Override public String toString()
    {   return getAddress()+ " телефон: " + getPhone();
    }
    public String getAddress ()
    {                       // RU, 125433, МО, Москва, Королёва 12/4, 192,
        return String.format ("%s, %s, %s, %s, %s, %s.",
                              countryCode, postalCode, region, townVillage, streetHouse, apartment);
    }
/*    public static ShippingInfoDto dummyShippingInfoDto ()
    {
        ShippingInfoDto sidto = new ShippingInfoDto();
        sidto.countryCode = "RU";
        sidto.postalCode = STR_EMPTY;
        //sidto.region = ;
        sidto.townVillage = STR_EMPTY;
        sidto.streetHouse = STR_EMPTY;
        //sidto.apartment = ;
        return sidto;
    }*/
}
