package ru.gb.antonov.j710.order.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.gb.antonov.j710.order.dtos.ShippingInfoDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Locale;

import static ru.gb.antonov.j710.monolith.Factory.STR_EMPTY;

@Entity
@Data
@NoArgsConstructor
@Table (name="shipping_info")
public class ShippingInfo {

    @Id    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column (name="country_code", nullable=false, length=2)
    private String countryCode = STR_EMPTY;

    @Column (name="postal_code", nullable=false, length=6)
    private String postalCode = STR_EMPTY;

    @Column (name="region", nullable=false, length=100)
    private String region = STR_EMPTY;

    @Column (name="town_village", nullable=false, length=100)
    private String townVillage = STR_EMPTY;

    @Column (name="street_house", nullable=false, length=100)
    private String streetHouse = STR_EMPTY;

    @Column (name="apartment", nullable=false, length=100)
    private String apartment = STR_EMPTY;

    @Column (name="phone", nullable=false, length=20)
    private String phone = STR_EMPTY;

    @CreationTimestamp    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp    @Column (name="updated_at")
    private LocalDateTime updatedAt;
//-------------------------------------------------------------------------

    public static ShippingInfo fromShippingInfoDto (ShippingInfoDto dto)    {
        ShippingInfo si = new ShippingInfo();
        if (dto != null) {
            si.countryCode = setOrEmpty (dto.getCountryCode());
            si.postalCode  = setOrEmpty (dto.getPostalCode());
            si.region      = setOrEmpty (dto.getRegion());
            si.townVillage = setOrEmpty (dto.getTownVillage());
            si.streetHouse = setOrEmpty (dto.getStreetHouse());
            si.apartment   = setOrEmpty (dto.getApartment());
            si.phone       = setOrEmpty (dto.getPhone());
        }
        return si;
    }
    private static String setOrEmpty (String value) {   return (value == null) ? STR_EMPTY : value;   }
//-------------------------------------------------------------------------
    private void setId (Long value) { id = value; }
    private void setUpdatedAt (LocalDateTime value) { updatedAt = value; }
    private void setCreatedAt (LocalDateTime value) { createdAt = value; }
//-------------------------------------------------------------------------
/** Приводим в порядок некоторые поля. */
    public ShippingInfo adjust ()    {
        countryCode = countryCode.trim().toUpperCase(Locale.ROOT);
        return this;
    }

    @Override public String toString()    {   return getAddress()+ " телефон: " + getPhone();    }

    public String getAddress ()    {
                            // RU, 125433, MO, Москва, Королёва, 12/4, 192,
        return String.format ("%s, %s, %s, %s, %s, %s.",
            countryCode, postalCode, region, townVillage, streetHouse, apartment);
    }
}