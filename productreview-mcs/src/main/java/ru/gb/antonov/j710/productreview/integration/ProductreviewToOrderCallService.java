package ru.gb.antonov.j710.productreview.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gb.antonov.j710.order.entities.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductreviewToOrderCallService
{
    private final WebClient productServiceWebClient;

    public List<OrderItem> userOrderItemsByProductId_Payed (Long uid, Long pid)
    {
        String url = String.format ("/api/v1/order/payed_order_items/%d/%d", uid, pid);

        List<OrderItem> result = new ArrayList<>();
        List<?> collection = productServiceWebClient//
            .get()
            .uri (url)
            .retrieve()
            .bodyToMono (List.class)
            .block();

        if (collection != null && collection.size() > 0 && collection.get(0) instanceof OrderItem)
            result = (List<OrderItem>) collection;
        return result;
    }
}
