package ru.gb.antonov.j710.order.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.OurValidationException;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnauthorizedAccessException;
import ru.gb.antonov.j710.order.dtos.OrderDetalesDto;
import ru.gb.antonov.j710.order.dtos.OrderDto;
import ru.gb.antonov.j710.order.entities.OrderItem;
import ru.gb.antonov.j710.order.services.OrderService;
import ru.gb.antonov.j710.order.services.OrderStatesService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.gb.antonov.j710.monolith.Factory.INAPP_HDR_LOGIN;

@RequestMapping ("/api/v1/order")
@RestController
@RequiredArgsConstructor
//@CrossOrigin ("*")
public class OrderController {

    private final OrderService       orderService;
    private final OrderStatesService orderStatesService;
//-------------------------------------------------------------------------------------------

/** Фронт запрашивает информацию по заказу, который пользователь собирается сделать.
На данный момент у нас есть только пользователь и его корзина. Нам предстоит определить,
как должен выглядеть заказ, и показать результат наших «исследований» пользователю для
подтверждения. */
    @GetMapping ("/details")
    public OrderDetalesDto getOrderDetales (@RequestHeader(name= INAPP_HDR_LOGIN) String username) {
    //в параметр username попадает значение заголовка username; если имя параметра отличается от имени заголовка, то нужно указать, из какого заголовка папраметр должен брать значение

        checkRightsToMakeOrder (username);
        return orderService.getOrderDetales (username);
    }

/** Пользователь оценил наше вИдение его заказа и нажал кнопку «Оформить заказ». */
    @PostMapping ("/confirm")
    @ResponseStatus (HttpStatus.CREATED)
    public OrderDetalesDto applyOrderDetails (@RequestBody @Validated OrderDetalesDto orderDetalesDto,
                                              BindingResult br,
                                              @RequestHeader(name= INAPP_HDR_LOGIN) String username)
    {   checkRightsToMakeOrder (username);
        if (br.hasErrors())
        {   //преобразуем набор ошибок в список сообщений, и пакуем в одно общее исключение (в наше заранее для это приготовленное исключение).
            List<String> list = br.getAllErrors().stream()
                                  .map (ObjectError::getDefaultMessage).collect (Collectors.toList());
            throw new OurValidationException (list);
        }
        return orderService.applyOrderDetails (orderDetalesDto, username);
    }

/** Фронт запрашивает список заказов пользователя. */
    @GetMapping ("/orders")
    public Collection<OrderDto> getOrders (@RequestHeader(name= INAPP_HDR_LOGIN) String username) {
        return orderService.getUserOrdersAsOrderDtos (username);
    }

    @GetMapping ("/payed_order_items/{uid}/{pid}")
    public Integer payedOrderItemsCountByUserIdAndProductId (@PathVariable Long uid, @PathVariable Long pid) {

        Integer stateId = orderStatesService.getOrderStatePayed().getId();
        List<OrderItem> list = orderService.userOrderItemsByProductId (uid, pid, stateId);
        return list.size();
    }

/** Проверяем, зарегистрирован ли пользователь и бросаем исключение, если он не зарегистрирован.
    @throws UnauthorizedAccessException */
    private void checkRightsToMakeOrder (String username) {

        if (username == null || username.isBlank())
            throw new UnauthorizedAccessException ("Заказ может оформить только авторизованый пользователь. (It's only authorized user can make order.)");
    }
}
