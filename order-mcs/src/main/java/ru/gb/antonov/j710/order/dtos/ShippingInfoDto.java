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

    @Length (min=DELIVERING_COUNTRYCODE_LEN, max=DELIVERING_COUNTRYCODE_LEN, message="Код страны: 2 латинских символа.")
    private String countryCode = "RU";

    @Length (min=DELIVERING_POSTALCODE_LEN, max=DELIVERING_POSTALCODE_LEN, message="Почтовый индекс: 6 цифр.")
    private String postalCode;

    @Length (max=DELIVERING_REGION_LEN_MAX, message = "\rНазвание региона (субъекта федерации): до 60 символов.")
    private String region;

    @Length (max=DELIVERING_TOWN_VILLAGE_LEN_MAX, message="\rНазвание населённого пункта: до 100 символов.")
    private String townVillage;

    @Length (max=DELIVERING_STREET_HOUSE_LEN_MAX, message="\rУлица и номер дома: до 100 символов.")
    private String streetHouse;

    @Length (max=DELIVERING_APARTMENT_LEN_MAX, message="\rНомер квартиры/офиса: до 20 символов.")
    private String apartment;

    @NotNull (message="\rУкажите номер телефона.")
    @Length (min=DELIVERING_PHONE_LEN_MIN, max=DELIVERING_PHONE_LEN_MAX, message="\rНомер телефона: 1…20 символов.")
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
}