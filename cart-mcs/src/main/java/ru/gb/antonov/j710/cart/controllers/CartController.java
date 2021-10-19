package ru.gb.antonov.j710.cart.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.cart.services.CartService;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;
import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;
import ru.gb.antonov.j710.monolith.entities.dtos.StringResponse;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping ("/api/v1/cart")    //http://localhost:8191/market-cart/api/v1/cart
@RequiredArgsConstructor
@CrossOrigin ("*")
public class CartController
{
    private final CartService cartService;
//------------------------------------------------------------------------

    @GetMapping({"/uuid"})
    public StringResponse generateCartUuid ()
    {
        return new StringResponse (UUID.randomUUID().toString());
    }

    @GetMapping ("/{uuid}")
    public CartDto getProductsCart (@RequestHeader(required = false) String username/*Principal principal*/, @PathVariable String uuid)
    {
        //return cartService.getUsersCartDto (principal, uuid);
        return cartService.getUsersCartDto (username, uuid);
    }

    @GetMapping ("/load/{uuid}")
    public Integer getCartLoad (@RequestHeader(required = false) String username/*Principal principal*/, @PathVariable String uuid)
    {
        //return cartService.getCartLoad (principal, uuid);
        return cartService.getCartLoad (username, uuid);
    }

/*    @GetMapping ("/cost/{uuid}")
    public Double getCartCost (Principal principal, @PathVariable String uuid)
    {
        return cartService.getCartCost (principal, uuid);
    }*/

    @GetMapping ("/plus/{productId}/{uuid}")
    public void increaseProductQuantity (/*Principal principal*/@RequestHeader(required = false) String username, @PathVariable Long productId, @PathVariable String uuid)
    {
        if (productId == null)
            throw new UnableToPerformException ("Не могу изменить количество для товара id: "+ productId);
        //cartService.changeProductQuantity (principal, uuid, productId, 1);
        cartService.changeProductQuantity (username, uuid, productId, 1);
    }

    @GetMapping ("/minus/{productId}/{uuid}")
    public void decreseProductQuantity (/*Principal principal*/@RequestHeader(required = false) String username, @PathVariable Long productId, @PathVariable String uuid)
    {
        if (productId == null)
            throw new UnableToPerformException ("Не могу изменить количество для товара id: "+ productId);
        //cartService.changeProductQuantity (principal, uuid, productId, -1);
        cartService.changeProductQuantity (username, uuid, productId, -1);
    }

    @GetMapping ("/remove/{productId}/{uuid}")
    public void removeProduct (/*Principal principal*/@RequestHeader(required = false) String username, @PathVariable Long productId, @PathVariable String uuid)
    {
        if (productId == null)
            throw new UnableToPerformException ("Не могу удалить из корзины товар id: "+ productId);
        //cartService.removeProductFromCart (principal, uuid, productId);
        cartService.removeProductFromCart (username, uuid, productId);
    }

    @GetMapping ("/clear/{uuid}")
    public void clearCart (/*Principal principal*/@RequestHeader(required = false) String username, @PathVariable String uuid)
    {
        //cartService.clearCart (principal, uuid);
        cartService.clearCart (username, uuid);
    }

    @GetMapping({"/merge/{uuid}"})
    public void mergeCarts (/*Principal principal*/@RequestHeader String username, @PathVariable String uuid)
    {
        //this.cartService.mergeCarts (principal, uuid);
        this.cartService.mergeCarts (username, uuid);
    }

    @GetMapping ("/drycart/{username}")
    public CartDto getDryCart (@PathVariable String username)
    {
        return cartService.getUsersDryCartDto (username);
    }

    @GetMapping ("/remove_non_empty/{username}")
    public void removeNonEmptyItems (@PathVariable String username)
    {
        cartService.removeNonEmptyItems (username);
    }
}
