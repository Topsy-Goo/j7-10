package ru.gb.antonov.j710.cart.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.cart.services.CartService;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;
import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;
import ru.gb.antonov.j710.monolith.entities.dtos.StringResponse;

import java.util.UUID;

@RestController
@RequestMapping ("/api/v1/cart")    //http://localhost:8191/cart/api/v1/cart
@RequiredArgsConstructor
//@CrossOrigin ("*")
public class CartController
{
    private final CartService cartService;
//------------------------------------------------------------------------

/** Запрос на генерирование UUID. Авторизация НЕ требуется. */
    @GetMapping({"/generate_uuid"})                             //-
    public StringResponse generateCartUuid ()
    {
        return new StringResponse (UUID.randomUUID().toString());
    }

/** Запрос корзины авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/{uuid}")                                     //±
    public CartDto getProductsCart (@RequestHeader(required = false) String username, @PathVariable String uuid)
    {
        return cartService.getUsersCartDto (username, uuid);
    }

/** Запрос количества товаро в корзине авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/load/{uuid}")                                //±
    public Integer getCartLoad (@RequestHeader(required = false) String username, @PathVariable String uuid)
    {
        return cartService.getCartLoad (username, uuid);
    }

/** Увеличение кол-ва товара в корзине авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/plus/{productId}/{uuid}")                    //±
    public void increaseProductQuantity (@RequestHeader(required = false) String username, @PathVariable Long productId, @PathVariable String uuid)
    {
        if (productId == null)
            throw new UnableToPerformException ("Не могу изменить количество для товара id: "+ productId);
        cartService.changeProductQuantity (username, uuid, productId, 1);
    }

/** Уменьшение кол-ва товара в корзине авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/minus/{productId}/{uuid}")                   //±
    public void decreseProductQuantity (@RequestHeader(required = false) String username, @PathVariable Long productId, @PathVariable String uuid)
    {
        if (productId == null)
            throw new UnableToPerformException ("Не могу изменить количество для товара id: "+ productId);
        cartService.changeProductQuantity (username, uuid, productId, -1);
    }

/** Удаление товарной позиции из корзины авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/remove/{productId}/{uuid}")                  //±
    public void removeProduct (@RequestHeader(required = false) String username, @PathVariable Long productId, @PathVariable String uuid)
    {
        if (productId == null)
            throw new UnableToPerformException ("Не могу удалить из корзины товар id: "+ productId);
        cartService.removeProductFromCart (username, uuid, productId);
    }

/** Удаление всех товарных позиций из корзины авторизованного пользователя или гостя.
Авторизация нужна для пользователя, но не нужна для гостя. */
    @GetMapping ("/clear/{uuid}")                               //±
    public void clearCart (@RequestHeader(required = false) String username, @PathVariable String uuid)
    {
        cartService.clearCart (username, uuid);
    }

/** Запрос на слияние корзин авторизованного пользователя и гостя.
Авторизация нужна.  */
    @GetMapping({"/merge/{uuid}"})                              //+
    public void mergeCarts (@RequestHeader String username, @PathVariable String uuid)
    {
        this.cartService.mergeCarts (username, uuid);
    }

/** Сейчас вызывается из модуля order-mcs. */
    @GetMapping ("/drycart/{username}")                         //-
    public CartDto getDryCart (@PathVariable String username)
    {
        return cartService.getUsersDryCartDto (username);
    }

/** Сейчас вызывается из модуля order-mcs. */
    @GetMapping ("/remove_non_empty/{username}")                //-
    public void removeNonEmptyItems (@PathVariable String username)
    {
        cartService.removeNonEmptyItems (username);
    }
}
