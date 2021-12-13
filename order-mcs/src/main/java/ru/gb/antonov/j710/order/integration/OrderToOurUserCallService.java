package ru.gb.antonov.j710.order.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OrderToOurUserCallService {

    private final WebClient ourUserServiceWebClient;

    public Long userIdByLogin (String login)     {
        return ourUserServiceWebClient.get()
                                      .uri ("/api/v1/user_profile/userid/" + login)
                                      .retrieve()
                                      .bodyToMono (Long.class)
                                      .block(/*Duration.ofSeconds (10)*/);
    }

    public String userNameByUserId (Long uid)    {
        return ourUserServiceWebClient.get()
                                      .uri ("/api/v1/user_profile/username/" + uid)
                                      .retrieve()
                                      .bodyToMono (String.class)
                                      .block(/*Duration.ofSeconds (10)*/);
    }
}