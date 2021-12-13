package ru.gb.antonov.j710.cart.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class CartToOurUserCallService {

    private final WebClient ourUserServiceWebClient;

    public Long userIdByLogin (String login)    {
        return ourUserServiceWebClient.get() //< метод (тип запроса: GET, POST, …)
                                      .uri ("/api/v1/user_profile/userid"/* + login*/)
                                      .retrieve() //< хотим получить ответ
                                      .bodyToMono (Long.class) //< придёт 1 объект, и он может быть обработан в отдельном потоке
                                      //.bodyToFlux (Long.class) < (альтернатива для bodyToMono) ждём поступление объектов (они могут придти НЕ одновременно)
                                      .block(); //< блокируем выполнение задачи до получения ответа (и возвращаемся в синхронный режим после получения всего объекта)
                                      //.subscribe(o->{…}); < (альтернатива для block) выполнять над каждым полученнм объектом какое-то действие
    //(Если ответ не пришёл по истечении таймаута, оговоренного при создании WebClient-а, то, возможно, придётся обрабатывать исключение.)
    }
}