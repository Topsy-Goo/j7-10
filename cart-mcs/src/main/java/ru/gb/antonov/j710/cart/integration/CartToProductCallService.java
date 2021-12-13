package ru.gb.antonov.j710.cart.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CartToProductCallService {

    private final WebClient productServiceWebClient;

    public ProductDto getProductById (Long pid)    {
        return productServiceWebClient.get()
                                      .uri ("/api/v1/products/" + pid)
                                      .retrieve()
                                      .bodyToMono (ProductDto.class)
                                      .block();
    }

    public BigDecimal productPriceById (Long pid) {   return getProductById(pid).getPrice();   }
}