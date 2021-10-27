package ru.gb.antonov.j710.productreview.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ProductreviewToOrderCallService
{
    private final WebClient orderServiceWebClient;

    public Integer payedOrderItemsCountByUserIdAndProductId (Long uid, Long pid)
    {
        Integer result = null;
        if (uid != null && pid != null)
        {
            result = orderServiceWebClient//
                      .get()
                      .uri (String.format ("/api/v1/order/payed_order_items/%d/%d", uid, pid))
                      .retrieve()
                      .bodyToMono (Integer.class)
                      .block();
        }
        return result == null ? 0 : result;
    }
}
