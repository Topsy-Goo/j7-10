package ru.gb.antonov.j710.monolith;

import org.springframework.core.env.Environment;
import org.springframework.util.MultiValueMap;
import ru.gb.antonov.j710.monolith.entities.Product;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Factory
{
    public static final boolean DRYCART = true;
    public static final boolean FLOAT_POINT = true; //< в isDecimalNumber() указывает, нужно ли считать точку/запятую частью числа

    public static       BigDecimal MIN_PRICE = BigDecimal.valueOf (0.00);
    public static final BigDecimal MAX_PRICE = BigDecimal.valueOf (Double.MAX_VALUE);

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
    public static final String PRODUCT_TITLE_FIELD_NAME = "title";
    public static final String PRODUCT_PRICE_FIELD_NAME = "price";
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
    static {
        checkClassForPresenceOfFields (Product.class, PRODUCT_TITLE_FIELD_NAME, PRODUCT_PRICE_FIELD_NAME);
    }

    public static void init (Environment env)    {

        System.out.println ("\n************************* Считывание настроек: *************************");

        String s;
        if (isDecimalNumber (s = env.getProperty ("views.shop.page.items"), !FLOAT_POINT)) {

            PROD_PAGESIZE_DEF = Integer.parseInt (s);
            System.out.println ("views.shop.page.items: "+ PROD_PAGESIZE_DEF);
        }
/*         if (isDecimalNumber (s = env.getProperty ("app.product.price.min"), FLOAT_POINT)) {

           MIN_PRICE = BigDecimal.valueOf(Double.parseDouble(s));
            if (MIN_PRICE.compareTo(BigDecimal.ZERO) < 0)
                MIN_PRICE = BigDecimal.ZERO;
            System.out.println ("app.product.price.min: "+ MIN_PRICE);
        }*/
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

/** Пробуем преобразовать строку в Double. Если не получилось, то пробуем преобразовать в Integer, а
потом в Double.
@param s строка-число
@return double
@throws NumberFormatException если строка s не может быть преобразована к числу. */
    public static double stringToDouble (String s) {
        if (s == null || s.isBlank())
            throw new NumberFormatException();
        double result;
        s = s.trim();
        try {
            result = Double.parseDouble (s);
        }
        catch (NumberFormatException e) {
            result = Integer.valueOf (s).doubleValue();
        }
        return result;
    }

/** Пробуем преобразовать строку в Integer. Если не получилось, то пробуем преобразовать в Double, а
потом в Integer.
@param s строка-число
@return int.
@throws NumberFormatException если строка s не может быть преобразована к числу. */
    public static int stringToInteger (String s) {
        if (s == null || s.isBlank())
            throw new NumberFormatException();
        int result;
        s = s.trim();
        try {
            result = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            result = Double.valueOf (s).intValue();
        }
        return result;
    }

/** Проверка на наличие в классе {@code tClass} полей с указанными именами. Метод предназначен для проверки на
стадии запуска приложения; при пом.исключения метод сигнализирует о том, что было изменено имя поля класса,
 без согласования с методами или переменными, которые это имя используют.
@param tClass класс, в котором проверяется наличие поля с указанным именем.
@param fieldNames имена полей, наличие которых требуется проверить.
@throws RuntimeException если хотя бы одно поле с именем из fieldNames не нашлось. */
    public static <T> void checkClassForPresenceOfFields (Class<T> tClass, String... fieldNames) {

        if (fieldNames != null && fieldNames.length > 0) {
            List<String> classFields = Arrays.stream (tClass.getDeclaredFields())
                                             .map (Field::getName)
                                             .collect(Collectors.toList());
            List<String> names = new ArrayList<>(Arrays.asList(fieldNames));

            names.removeIf (classFields::contains);
            if (!names.isEmpty())
                throw new RuntimeException (String.format ("\nОШИБКА! В классе %s не найдены поля:\n\t%s\n",
                                            tClass.getName(), names));
        }
    }
}
