package ru.gb.antonov.j710.cart.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CartToOurUserCallService
{
    private final RestTemplate restTemplate;

    @Value ("${integration.user-service.url}")
    private String userServiceUrl;

    public Long userIdByLogin (String login)
    {
        return restTemplate.getForObject (
                    userServiceUrl + "/api/v1/user_profile/userid"/* + login*/, Long.class);
    }
}