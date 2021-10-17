package ru.gb.antonov.j710.monolith.beans.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.OurValidationException;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnauthorizedAccessException;
import ru.gb.antonov.j710.monolith.beans.services.OrderService;
import ru.gb.antonov.j710.monolith.beans.services.OurUserService;
import ru.gb.antonov.j710.monolith.entities.dtos.OrderDetalesDto;

import java.security.Principal;
import java.util.stream.Collectors;

@RequestMapping ("/api/v1/order")
@RestController
@RequiredArgsConstructor
public class OrderController
{
    private final OrderService   orderService;
    private final OurUserService ourUserService;
//-------------------------------------------------------------------------------------------

    @GetMapping ("/details")
    public OrderDetalesDto getOrderDetales (Principal principal)
    {
        checkRightsToMakeOrder (principal);
        return orderService.getOrderDetales (principal);
    }

    @PostMapping ("/confirm")
    @ResponseStatus (HttpStatus.CREATED)
    public OrderDetalesDto applyOrderDetails (@RequestBody @Validated OrderDetalesDto orderDetalesDto,
                                              BindingResult br, Principal principal)
    {   checkRightsToMakeOrder (principal);
        if (br.hasErrors())
        {   //преобразуем набор ошибок в список сообщений, и пакуем в одно общее исключение (в наше заранее для это приготовленное исключение).
            throw new OurValidationException (br.getAllErrors().stream()
                                                .map (ObjectError::getDefaultMessage)
                                                .collect (Collectors.toList ()));
        }
        return orderService.applyOrderDetails (orderDetalesDto, principal);
    }

/** Проверяем, зарегистрирован ли пользователь и бросаем исключение, если он не зарегистрирован.
    @throws UnauthorizedAccessException */
    private void checkRightsToMakeOrder (Principal principal)
    {
        if (principal == null)
            throw new UnauthorizedAccessException ("Заказ может оформить только авторизованый пользователь (It's only authorized user can make order.).");
    }
}
