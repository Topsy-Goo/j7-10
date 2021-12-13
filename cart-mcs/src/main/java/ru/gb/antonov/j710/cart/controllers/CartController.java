package ru.gb.antonov.j710.cart.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.cart.services.CartService;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;
import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;
import ru.gb.antonov.j710.monolith.entities.dtos.StringResponse;

import java.util.UUID;
import java.util.logging.Logger;

import static ru.gb.antonov.j710.monolith.Factory.INAPP_HDR_LOGIN;

@RestController
@RequestMapping ("/api/v1/cart")    //http://localhost:8191/cart/api/v1/cart
@RequiredArgsConstructor
//@CrossOrigin ("*")
public class CartController {

    private final        CartService cartService;
    private final static Logger      LOGGER = Logger.getLogger("ru.gb.antonov.j71.beans.controllers.CartController");
//------------------------------------------------------------------------

/** Запрос на генерирование UUID. Авторизация НЕ требуется. */
    @GetMapping({"/generate_uuid"})                             //-
    public StringResponse generateCartUuid ()    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/generate_uuid");
        return new StringResponse (UUID.randomUUID().toString());
    }

/** Запрос корзины авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/{uuid}")                                     //±
    public CartDto getProductsCart (
                        @RequestHeader(name= INAPP_HDR_LOGIN, required = false) String username,
                        @PathVariable String uuid)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/"+ uuid +"; username: "+ username);
        return cartService.getUsersCartDto (username, uuid);
    }

/** Запрос количества товаро в корзине авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/load/{uuid}")                                //±
    public Integer getCartLoad (
                        @RequestHeader(name= INAPP_HDR_LOGIN, required = false) String username,
                        @PathVariable String uuid)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/load/"+ uuid +"; username: "+ username);
        return cartService.getCartLoad (username, uuid);
    }

/** Увеличение кол-ва товара в корзине авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/plus/{productId}/{uuid}")                    //±
    public void increaseProductQuantity (
                        @RequestHeader(name= INAPP_HDR_LOGIN, required = false) String username,
                        @PathVariable Long productId, @PathVariable String uuid)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/plus/"+ productId +"/"+ uuid +"; username: "+ username);
        if (productId == null)
            throw new UnableToPerformException ("Не могу изменить количество для товара id: "+ productId);
        cartService.changeProductQuantity (username, uuid, productId, 1);
    }

/** Уменьшение кол-ва товара в корзине авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/minus/{productId}/{uuid}")                   //±
    public void decreseProductQuantity (
                        @RequestHeader(name= INAPP_HDR_LOGIN, required = false) String username,
                        @PathVariable Long productId, @PathVariable String uuid)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/minus/"+ productId +"/"+ uuid +"; username: "+ username);
        if (productId == null)
            throw new UnableToPerformException ("Не могу изменить количество для товара id: "+ productId);
        cartService.changeProductQuantity (username, uuid, productId, -1);
    }

/** Удаление товарной позиции из корзины авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/remove/{productId}/{uuid}")                  //±
    public void removeProduct (@RequestHeader(name= INAPP_HDR_LOGIN, required = false) String username,
                               @PathVariable Long productId, @PathVariable String uuid)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/remove/"+ productId +"/"+ uuid +"; username: "+ username);
        if (productId == null)
            throw new UnableToPerformException ("Не могу удалить из корзины товар id: "+ productId);
        cartService.removeProductFromCart (username, uuid, productId);
    }

/** Удаление всех товарных позиций из корзины авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/clear/{uuid}")                               //±
    public void clearCart (@RequestHeader(name= INAPP_HDR_LOGIN, required = false) String username,
                           @PathVariable String uuid)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/clear/"+ uuid +"; username: "+ username);
        cartService.clearCart (username, uuid);
    }

/** Запрос на слияние корзин авторизованного пользователя и гостя.
Авторизация нужна.  */
    @GetMapping({"/merge/{uuid}"})                              //+
    public void mergeCarts (@RequestHeader(name= INAPP_HDR_LOGIN) String username,
                            @PathVariable String uuid)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/merge/"+ uuid +"; username: "+ username);
        this.cartService.mergeCarts (username, uuid);
    }

/** Сейчас вызывается из модуля order-mcs. */
    @GetMapping ("/drycart/{username}")                         //-
    public CartDto getDryCart (@PathVariable(name= INAPP_HDR_LOGIN) String username)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/drycart/"+ username);
        return cartService.getUsersDryCartDto (username);
    }

/** Сейчас вызывается из модуля order-mcs. */
    @GetMapping ("/remove_non_empty/{username}")                //-
    public void removeNonEmptyItems (@PathVariable(name= INAPP_HDR_LOGIN) String username)    {

        LOGGER.info ("Получен GET-запрос: /api/v1/cart/remove_non_empty/"+ username);
        cartService.removeNonEmptyItems (username);
    }
}
