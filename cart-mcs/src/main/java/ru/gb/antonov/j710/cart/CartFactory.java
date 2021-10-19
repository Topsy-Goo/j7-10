package ru.gb.antonov.j710.cart;

import org.springframework.core.env.Environment;
import ru.gb.antonov.j710.monolith.Factory;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;

import java.time.Duration;

import static ru.gb.antonov.j710.monolith.Factory.*;

public class CartFactory
{
    public static final String STR_EMPTY = "";
    public static String CART_PREFIX_ = STR_EMPTY;
    public static Duration CART_LIFE = Duration.ofDays (30L);
    public static Duration DONOT_SET_CART_LIFE = null;

/** Считываем настройки из файла настроек. */
    public static void init (Environment env)
    {
        System.out.println ("\n************************* Считывание настроек: *************************");

        CART_PREFIX_ = env.getProperty ("app.cart.prefix");
        System.out.println ("app.cart.prefix: "+ CART_PREFIX_);

        String s = env.getProperty ("app.cart.life");
        if (Factory.isDecimalNumber (s, !FLOAT_POINT))
        {
            CART_LIFE = Duration.ofDays(Long.parseLong(s));
            System.out.println ("app.cart.life: "+ CART_LIFE);
        }
        System.out.println ("************************** Настройки считаны: **************************");
    }

/** Составляем ключ как:  GB_RU_J7_WEBSHOP_ + login
*/
    public static String cartKeyByLogin (String login)
    {
        String postfix = validateString (login, LOGIN_LEN_MIN, LOGIN_LEN_MAX);
        if (postfix != null)
            return CART_PREFIX_ + postfix;
        throw new UnableToPerformException("cartKeyByLogin(): некорректный логин: " + login);
    }
}
