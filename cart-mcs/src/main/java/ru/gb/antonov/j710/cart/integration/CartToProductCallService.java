package ru.gb.antonov.j710.cart.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

@Component
@RequiredArgsConstructor
public class CartToProductCallService
{
    private final RestTemplate restTemplate;

    @Value("${integration.product-service.url}")
    private String productServiceUrl;

    public ProductDto getProductById (Long pid)
    {   return restTemplate.getForObject (productServiceUrl + "/api/v1/products/" + pid, ProductDto.class);
    }

    public Double productPriceById (Long pid) {   return getProductById (pid).getPrice();   }
}