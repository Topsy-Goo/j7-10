package ru.gb.antonov.j710.cart.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j710.cart.CartFactory;
import ru.gb.antonov.j710.cart.integration.CartToOurUserCallService;
import ru.gb.antonov.j710.cart.integration.CartToProductCallService;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.ResourceNotFoundException;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;
import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;
import ru.gb.antonov.j710.monolith.entities.dtos.OrderItemDto;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import static ru.gb.antonov.j710.cart.CartFactory.*;
import static ru.gb.antonov.j710.monolith.Factory.*;

@Service
@RequiredArgsConstructor
public class CartService {

/** <b>Как устроена корзина</b><p>
        При запуске приложения на фронте, клиент ищет строковую переменную {@code gbj7MarketGuestCartId}
 (UUID) в харнилище браузера и, если не находит, запрашивает для неё значение на бэке. Во все «корзинные»
 запросы клиент вставляет этот UUID, независимо от того, авторзован юзер или нет.<p>
        В запросах клиента к бэку логин юзера указывает на одну корзину, а UUID на другую. Если юзер
 авторизован, используется первая из корзин. Если неавторизован — вторая.<p>
        При авторизации юзера ВСЕ товары из UUID-корзины перемещаются в логин-корзину, после чего
 UUID-корзина удаляется, но её UUID остаётся у клиента. UUID-корзина создаётся только по мере
 необходимости. До этого момента реально существует только UUID для неё. UUID-корзина, в отличие от
 логин-корзины, имеет таймер бездействи, по истечении которого она удаляется, но её UUID остаётся у
 клиента.<p>
        Оформить заказ можно только будучи авторизованным.<p>
        Имя корзины состоит из прификса и постфикса. Префикс одинаков для всех корзин приложения, а
 постфиксом является логин или UUID. Корзины хранятся в Memurai-кэше (аналог Redis'а), а имя корзины
 является ключом для поиска корзины в этом кэше.<p>
        Любая корзина реализована в виде 2 частей: InMemoryCart (хранится в Memurai-е) и CartDto
 (формируется и отдаётся клиенту каждый раз, когда он запрашивает информацию о корзине).<p>
        В InMemoryCart храним только id товаров и количества. Всю остальную инф-цию о товаре считываем
 из базы во время формирования CartDto. Таким образом мы решаем проблему устаревших цен и названий для
 товаров. (Если этот подход слишком нагружает БД, то, возможно, БД тоже нужен какой-то кэшь.)
*/
    private final RedisTemplate<String, Object> redisTemplate;
    private final CartToProductCallService      cartToProductCallService;
    private final CartToOurUserCallService      cartToOurUserCallService;
//--------------------- классы для хранения корзин в кэше ----------------

    public static class CartsEntry { //Это не Entry; просто носить ключ рядом с корзиной очень удобно.

        private final String key;
        private final InMemoryCart imcart;

        private CartsEntry (String k, InMemoryCart v) {  key = k;    imcart = v;  }
    }

    public static class CartItem {

        private long pid;
        private int quantity;

        public CartItem (){}
        private CartItem (long p, int q) { pid = p;   quantity = q; }

        public void setPid (long value) { pid = value; }
        public void setQuantity (int value) { quantity = Math.max(value, 0); }

        public long getPid () { return pid; }
        public int getQuantity () { return quantity; }

        public String toString() { return String.format ("CartItem:[pid:%d, qty:%d]", pid, quantity); }
    }

    public static class InMemoryCart {

        List<CartItem> citems;

        private InMemoryCart () { citems = new LinkedList<>(); }

        public void setCitems (List<CartItem> list) { citems = list; }
        public List<CartItem> getCitems () { return citems; }

/** @return {@code true}, только если корзина не была пустой в момент вызова метода. */
        private boolean clear() {

            boolean ok = citems.size() > 0;
            citems.clear();
            return ok;
        }

/** Добавляем к орзину элемент, даже если его кол-во ==0.
    @return {@code true}, только если корзина была изменена: добавлен новый элемент или увеличено количество уже существующего элемента.
*/
        private boolean add (@NotNull CartItem cartItem) {

            long p = cartItem.pid;
            int q = cartItem.quantity;
            if (q >= 0) {

                CartItem ci = getItemByPid (p);
                if (ci != null) {

                    ci.quantity += q;
                    return q > 0;
                }
                return citems.add (new CartItem (p, q));
            }
            return false;
        }

        private int calcLoad () {

            int load = 0;
            for (CartItem ci : citems)
                load += ci.quantity;
            return load;
        }

        private CartItem getItemByPid (long pid) {

            for (CartItem ci : citems)
                if (ci.pid == pid)
                    return ci;
            return null;
        }

        /** Метод должен вызываться в рамках к.-л. транзакции. */
/*        private BigDecimal calcCost (CartService cs) {

            BigDecimal cartcost = BigDecimal.ZERO;
            for (CartItem ci : citems) {

                int quantity = ci.quantity;
                if (quantity > 0) {
                    BigDecimal price = cs.inlineGetProductPrice (ci.pid);
                    cartcost = cartcost.add (price.multiply (BigDecimal.valueOf (quantity)));
                }
            }
            return cartcost;
        }*/

        private boolean removeNonEmptyItems() { return citems.removeIf (ci->ci.quantity > 0); }

        private boolean removeItemByProductId (long pid)  { return citems.removeIf (ci->ci.pid == pid); }

        public String toString() { return citems.toString(); }
    }
//------------------------------------------------------------------------

/*    private BigDecimal inlineGetProductPrice (Long pid) {
        return cartToProductCallService.getProductPriceById (pid);
    }*/

    @Transactional @NotNull
    public CartDto getUsersCartDto (String username, String uuid) {

        CartsEntry ce = getUsersCartEntry (username, uuid);
        return inMemoryCartToDto (ce.imcart, !DRYCART);
    }

    @NotNull private CartsEntry getUsersCartEntry (String username, String uuid) {

        String postfix = uuid;
        Duration cartLife = CART_LIFE_GUEST;
        if (username != null) {

            postfix = username;
            cartLife = null;
        }
        return getUsersCartEntry (postfix, cartLife);
    }

    @NotNull private CartsEntry getUsersCartEntry (String postfix, Duration cartLife) {

        if ((postfix = validateString (postfix, LOGIN_LEN_MIN, LOGIN_LEN_MAX)) == null)
            throw new UnableToPerformException ("getUsersCart(): нет ключа — нет корзины!");

        String key = CartFactory.cartKeyByLogin (postfix);
        if (!redisTemplate.hasKey (key)) {

            redisTemplate.opsForValue().set (key, new InMemoryCart());
            if (cartLife != DONOT_SET_CART_LIFE)
                redisTemplate.expire (key, cartLife);
        }
        InMemoryCart imcart = (InMemoryCart) redisTemplate.opsForValue().get (key);
        if (imcart == null)
            throw new UnableToPerformException ("getUsersCart(): не могу извлечь корзину пользователя: " + postfix);

        return new CartsEntry (key, imcart);
    }

    private void updateCart (CartsEntry cartsEntry) {
        redisTemplate.opsForValue().set(cartsEntry.key, cartsEntry.imcart);
    }

    @Transactional
    public int getCartLoad (String username, String uuid) {
        return getUsersCartEntry (username, uuid).imcart.calcLoad();
    }

    @Transactional
    public void changeProductQuantity (String username, String uuid, long productId, int delta) {

/*  Считаем, что нет смысла создавать новую товарн.позицию для неположительных значений delta. Кроме того, мы не ждём, что нам придёт значение delta == 0.
    Товар не резервируем, при добавлении его в корзину. */
        if (delta == 0)
            return;
        boolean ok = false;
        ProductDto productDto = cartToProductCallService.getProductById (productId);

        CartsEntry ce = getUsersCartEntry (username, uuid);
        CartItem ci = ce.imcart.getItemByPid (productId);

        if (delta < 0) {

            if (ci != null) {

                ok = ci.quantity > 0;
                delta += ci.quantity;
                ci.quantity = (delta < 0) ? 0 : delta;
            }
        }
        else { //delta > 0

            int rest = productDto.getRest();
            if (rest > 0) {

                if (ci == null)
                    ce.imcart.citems.add (ci = new CartItem (productId, 0));

                int initialQuantity = ci.quantity;
                ci.quantity += delta;

                if (rest < ci.quantity)
                    ci.quantity = rest;
                ok = initialQuantity != ci.quantity;
            }
        }
        if (ok)
            updateCart (ce);
        else {
            String err = String.format ("не удалось изменить количество товара в корзине:\rтовар: %s\rостаток на складе: %d\rколичество этого товара в вашей корзине: %s",
                                        productDto.getTitle(), productDto.getRest(),
                                        (ci != null) ? ci.quantity : "?");
            throw new ResourceNotFoundException (err);
        }
    }

    @Transactional
    public void removeProductFromCart (String username, String uuid, long productId) {

        CartsEntry ce = getUsersCartEntry (username, uuid);
        if (ce.imcart.removeItemByProductId (productId))
            updateCart (ce);
    }

    @Transactional
    public void clearCart (String username, String uuid) { clearCart (getUsersCartEntry (username, uuid)); }

    private void clearCart (CartsEntry ce) {
        if (ce.imcart.clear())
            updateCart(ce);
    }

    @Transactional
    public CartDto getUsersDryCartDto (String login) {
        return inMemoryCartToDto (getUsersCartEntry (login, DONOT_SET_CART_LIFE).imcart, DRYCART);
    }

/** При формирвоании DTO-шки проверяем остаток товара «на складе». */
    @NotNull private CartDto inMemoryCartToDto (InMemoryCart imcart, boolean dtycart) {

        CartDto cdto = new CartDto();
        for (CartItem ci : imcart.citems) {

            int quantity = ci.quantity;
            if (quantity > 0 || !dtycart) {

                Long pid = ci.pid;
                ProductDto pdto = cartToProductCallService.getProductById (pid);
                int rest = pdto.getRest();
                if (rest > 0) {   //< если остаток товара <= 0, то считаем, что товара нет

                    quantity = Math.min (rest, quantity);
                    cdto.addItem (new OrderItemDto (pid, pdto.getCategory(), pdto.getTitle(),
                                                    pdto.getPrice(), rest, null, null,
                                                    pdto.getMeasure()),
                                  quantity);        //OrderItemDto.quantity будет установлено в CartDto.addItem.
                }
            }
        }
        return cdto;
    }

    @Transactional
    public void mergeCarts (String username, String uuid) {

        String postfixPr = null;
        String postfixUu = uuid == null ? null : validateString (uuid, LOGIN_LEN_MIN, LOGIN_LEN_MAX);

        if (postfixUu == null)
            throw new UnableToPerformException ("merge carts: не могу получить гостевую корзину.");

        if (/*principal*/username != null)
            postfixPr = validateString (username, LOGIN_LEN_MIN, LOGIN_LEN_MAX);

        if (postfixPr == null)
            throw new UnableToPerformException ("merge carts: вызов для НЕавторизованного пользователя.");

        if (redisTemplate.hasKey(CartFactory.cartKeyByLogin(postfixUu))) {

            CartsEntry ceUu = getUsersCartEntry (postfixUu, DONOT_SET_CART_LIFE);
            CartsEntry cePr = getUsersCartEntry (postfixPr, DONOT_SET_CART_LIFE); //< если корзины нет, она будет создана

            if (inlineMergeCarts (ceUu.imcart, cePr.imcart)) {

                updateCart (cePr);
                /*ceUu.imcart.clear();
                updateCart (ceUu);*/
                //redisTemplate.delete (ceUu.key); //< это не удаляет корзину из кэша! Кажется, дело в том, что удаление происходит во время транзакции (см.описание к RedisKeyCommands.del()), но это не точно.
                redisTemplate.expire (ceUu.key, CART_LIFE_DELETED); //< вместо удаления установим короткий (положительный) срок жизни (https://redis.io/commands/expire)
            }
        }
    }

/** В этом методе не проверяем остаток товара в товарной позиции. Сделаем это позже, при отправке корзины на фронт. */
    private boolean inlineMergeCarts (@NotNull InMemoryCart srcCart, @NotNull InMemoryCart dstCart) {

        boolean ok = false;
        for (CartItem ciS : srcCart.citems) {

            if (ciS.quantity >= 0)
                ok = dstCart.add (ciS);
        }
        return ok;
    }

    public void removeNonEmptyItems (String login) {

        CartsEntry ce = getUsersCartEntry (login, DONOT_SET_CART_LIFE);
        if (ce.imcart.removeNonEmptyItems())
            updateCart(ce);
    }

    @SuppressWarnings ("all")
    public boolean deleteCart (String postfix) {

        String key = CartFactory.cartKeyByLogin (postfix);
        if (!redisTemplate.hasKey (key))
            return redisTemplate.delete (getUsersCartEntry (postfix, DONOT_SET_CART_LIFE).key);
        return false;
    }
}
