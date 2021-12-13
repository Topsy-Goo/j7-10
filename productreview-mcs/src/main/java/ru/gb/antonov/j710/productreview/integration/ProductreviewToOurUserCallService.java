package ru.gb.antonov.j710.productreview.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class ProductreviewToOurUserCallService {

    private final WebClient ourUserServiceWebClient;

    public Long userIdByLogin (String login) {

        return ourUserServiceWebClient
                .get()
                .uri ("/api/v1/user_profile/userid/" + login)
                .retrieve()
                .bodyToMono (Long.class)
                .block();
    }

    public String userNameByUserId (Long uid) {

        String result = null;
        if (uid != null) {

            result = ourUserServiceWebClient//
                .get()
                .uri ("/api/v1/user_profile/username/"+ uid)
                .retrieve()
                .bodyToMono (String.class)
                .block();
        }
        return result;
    }
}
