package ru.gb.antonov.j710.order.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;

@Component
@RequiredArgsConstructor
public class OrderToCartCallService
{
    private final WebClient cartServiceWebClient;

    public CartDto getDryCartDto (String login)
    {
        return cartServiceWebClient.get()
                                   .uri ("/api/v1/cart/drycart/" + login)
                                   .retrieve()
                                   .bodyToMono (CartDto.class)
                                   .block();
    }

    public void removeNonEmptyItems (String login)
    {
        cartServiceWebClient.get()
                            .uri ("/api/v1/cart/remove_non_empty/" + login)
                            .retrieve()
                            .bodyToMono (void.class)
                            .block();
    }
}
