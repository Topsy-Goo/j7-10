package ru.gb.antonov.j710.users.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

@Component
@RequiredArgsConstructor
public class UserToProductCallService
{
    private final WebClient productServiceWebClient;

    public ProductDto getProductById (Long pid)
    {
        return productServiceWebClient
                .get()
                .uri ("/api/v1/products/" + pid)
                //.header ("header_name1", new String[]{"value1","value2","…"}) < можно добавить заголовки, и получатель вынет из них строковые данные при пом. @RequestHeader String header_name1.
                //.header ("header_name2", "…")
                .retrieve()
                .bodyToMono (ProductDto.class)
                //.toBodilessEntity()  < для запросов, которые не ждут объект в ответе.
                .block();
    }
}
