package ru.gb.antonov.j710.monolith;

import org.springframework.core.env.Environment;
import org.springframework.util.MultiValueMap;
import ru.gb.antonov.j710.monolith.entities.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Factory
{
    public static final boolean DRYCART = true;
    public static final boolean FLOAT_POINT = true; //< в isDecimalNumber() указывает, нужно ли считать точку/запятую частью числа

    public static       BigDecimal MIN_PRICE = BigDecimal.valueOf(0.01);
    public static final BigDecimal MAX_PRICE = BigDecimal.valueOf(Double.MAX_VALUE);

    public static int PROD_PAGESIZE_DEF = 6;
    public static final int PROD_TITLELEN_MIN   = 3;
    public static final int PROD_TITLELEN_MAX   = 255;
    public static final int PRODCAT_NAMELEN_MIN = 1;
    public static final int PRODCAT_NAMELEN_MAX = 255;
    public static final int LOGIN_LEN_MIN = 3;
    public static final int LOGIN_LEN_MAX = 36;
    public static final int PASS_LEN_MIN  = 3;
    public static final int PASS_LEN_MAX  = 128;
    public static final int EMAIL_LEN_MIN = 5;
    public static final int EMAIL_LEN_MAX = 64;
    public static final int DELIVERING_ADDRESS_LEN_MAX  = 255;
    public static final int DELIVERING_PHONE_LEN_MAX    = 20;
    public static final int DELIVERING_PHONE_LEN_MIN    = 1;
    public static final int ORDERSTATE_SHORTNAME_LEN    = 16;
    public static final int ORDERSTATE_FRIENDLYNAME_LEN = 64;
    public static final int DELIVERING_COUNTRYCODE_LEN      = 2;
    public static final int DELIVERING_POSTALCODE_LEN       = 6;
    public static final int DELIVERING_REGION_LEN_MAX       = 60;
    public static final int DELIVERING_TOWN_VILLAGE_LEN_MAX = 100;
    public static final int DELIVERING_STREET_HOUSE_LEN_MAX = 100;
    public static final int DELIVERING_APARTMENT_LEN_MAX    = 20;

    public static final String USE_DEFAULT_STRING = null;
    public static final String BRAND_NAME_ENG = "Marketplace";
    public static final String STR_EMPTY      = "";
    public static final String BEARER_        = "Bearer ";  //< должно совпадать с одноимённой переменной в gateway.GatewayApp
    public static final String PRODUCT_PRICE_FIELD_NAME = Product.getPriceFieldName();
    public static final String PRODUCT_TITLE_FIELD_NAME = Product.getTitleFieldName();
    public static final String ORDERSTATE_NONE     = "NONE";
    public static final String ORDERSTATE_PENDING  = "PENDING";
    public static final String ORDERSTATE_SERVING  = "SERVING";
    public static final String ORDERSTATE_PAYED    = "PAYED";
    public static final String ORDERSTATE_CANCELED = "CANCELED";
    public static final String ROLE_USER       = "ROLE_USER";
    public static final String ROLE_ADMIN      = "ROLE_ADMIN";
    public static final String ROLE_SUPERADMIN = "ROLE_SUPERADMIN";
    public static final String ROLE_MANAGER    = "ROLE_MANAGER";
    public static final String PERMISSION_EDIT_PRODUCT = "EDIT_PRODUCTS";
    public static final String PERMISSION_SHOPPING     = "SIMLE_SHOPPING";
    public static final String AUTHORIZATION_HDR_TITLE = "Authorization";  //< должно совпадать с одноимённой переменной в gateway.GatewayApp
    public static final String JWT_PAYLOAD_ROLES = "roles";    //< должно совпадать с одноимённой переменной в gateway.GatewayApp
    public static final String INAPP_HDR_LOGIN   = "username"; //< должно совпадать с одноимённой переменной в gateway.GatewayApp
    public static final String INAPP_HDR_ROLES   = "roles";    //< должно совпадать с одноимённой переменной в gateway.GatewayApp
    public static final String ORDER_IS_EMPTY = " Заказ пуст. ";
    public static final String ERR_MINPRICE_OUTOF_RANGE = "Указаная цена (%f)\rменьше минимальной (%f).";

    public static final MultiValueMap<String, String> NO_FILTERS = null;
    public static final Locale RU_LOCALE = new Locale ("ru", "RU");
    public static final String NO_STRING = null;
    //public static final MathContext MATH_CONTEXT2 = new MathContext(2);
//------------------------------------------------------------------------
    public static void init (Environment env)    {

        System.out.println ("\n************************* Считывание настроек: *************************");

        String s;
        if (isDecimalNumber (s = env.getProperty ("views.shop.page.items"), !FLOAT_POINT)) {

            PROD_PAGESIZE_DEF = Integer.parseInt (s);
            System.out.println ("views.shop.page.items: "+ PROD_PAGESIZE_DEF);
        }
        if (isDecimalNumber (s = env.getProperty ("app.product.price.min"), FLOAT_POINT)) {

            MIN_PRICE = BigDecimal.valueOf(Double.parseDouble(s));
            if (MIN_PRICE.compareTo(BigDecimal.ZERO) < 0)
                MIN_PRICE = BigDecimal.ZERO;
            System.out.println ("app.product.price.min: "+ MIN_PRICE);
        }
//        if (isDecimalNumber (s = env.getProperty (""), FLOAT_POINT))      ;
        System.out.println ("************************** Настройки считаны: **************************");
    }

/** Составляем строку даты и времени как:  {@code d MMMM yyyy, HH:mm:ss}<p>
    Пример строки:  {@code 20 сентября 2021, 23:10:29}
*/
    public static String orderCreationTimeToString (LocalDateTime ldt) {

        return (ldt != null) ? ldt.format(DateTimeFormatter.ofPattern ("d MMMM yyyy, HH:mm:ss", RU_LOCALE))
                             : "(?)";
    }

    public static boolean hasEmailFormat (String email) {

        boolean ok = false;
        int at = email.indexOf ('@');
        if (at > 0 && email.indexOf ('@', at +1) < 0) {

            int point = email.indexOf ('.', at);
            ok = point >= at +2;
        }
        return ok;
    }

    public static String  validateString (String s, int minLen, int maxLen) {

        if (s != null && minLen > 0 && minLen <= maxLen) {

            s = s.trim();
            int len = s.length();
            if (len >= minLen && len <= maxLen)
                return s;
        }
        return null;
    }

/** Проверяем, содержатся ли в строке посторонние символы, которые могут вызвать исключение при переводе строки вчисло. (Занятно, что разработчики Java не позаботились отакой мелочи.)
 @param s собственно срока.
 @param floatPoint {@code true} означает, что число может содержать десятичную точку или запятую. */
    public static boolean isDecimalNumber (String s, boolean floatPoint) {

        if (s == null)
            return false;

        char[] arrchar = s.trim().toCharArray();
        for (char ch : arrchar) {

            if (ch < '0' || ch > '9') {

                if (!floatPoint || (ch != '.' && ch != ','))
                    return false;
                floatPoint = false;
            }
        }
        return true;
    }

/** @param lines массив строк, подлежащих проверке.
    @return {@code true} — если ни одна из строк lines не пустая и не равна null. */
    public static boolean sayNoToEmptyStrings (String ... lines) {

        if (lines != null)
        for (String s : lines) {
            if (s == null || s.trim().isEmpty())
                return false;
        }
        return true;
    }

/** Пробуем преобразовать строку в Double.
@param s строка-число
@return null, если в процессе преобразования выяснилось, что строка s не может быть преобразована к числу. */
    public static Double stringToDouble (String s) {
        Double result = null;
        if (s != null && !s.isBlank())
        try {
            result = Double.valueOf (s.trim());
        }
        catch (NumberFormatException e) {
            Integer i = stringToInteger(s);
            result = (i != null) ? i.doubleValue() : null;
        }
        return result;
    }

/** Пробуем преобразовать строку в Integer.
@param s строка-число
@return null, если в процессе преобразования выяснилось, что строка s не может быть преобразована к числу. */
    public static Integer stringToInteger (String s) {
        Integer result = null;
        if (s != null && !s.isBlank())
        try {
            result = Integer.valueOf (s.trim());
        }
        catch (NumberFormatException e) {
            Double d = stringToDouble (s);
            result = (d != null) ? d.intValue() : null;
        }
        return result;
    }
}
