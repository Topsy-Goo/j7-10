package ru.gb.antonov.j710.order.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

@Component
@RequiredArgsConstructor
public class OrderToCartCallService
{
    private final RestTemplate restTemplate;

    @Value ("${integration.cart-service.url}")
    private String cartServiceUrl;

    public CartDto getDryCartDto (String login)
    {   return restTemplate.getForObject (cartServiceUrl + "/api/v1/cart/drycart/" + login, CartDto.class);
    }

    public void removeNonEmptyItems (String login)
    {   restTemplate.getForObject (cartServiceUrl + "/api/v1/cart/remove_non_empty/" + login, void.class);
    }
}
