package ru.gb.antonov.j710.order.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

@Component
@RequiredArgsConstructor
public class OrderToProductCallService {

    private final WebClient productServiceWebClient;

    public ProductDto getProductById (Long pid)   {

        return productServiceWebClient.get()
                                      .uri ("/api/v1/products/" + pid)
                                      .retrieve()
                                      .bodyToMono (ProductDto.class)
                                      .block();
    }
}