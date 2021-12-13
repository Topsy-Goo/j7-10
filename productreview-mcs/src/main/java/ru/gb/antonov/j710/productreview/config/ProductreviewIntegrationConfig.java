package ru.gb.antonov.j710.productreview.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class ProductreviewIntegrationConfig {

    //@Value ("${integration.product-service.url}") private String productServiceUrl;
    @Value ("${integration.user-service.url}")    private String ourUserServiceUrl; //+
    @Value ("${integration.order-service.url}")   private String orderServiceUrl;   //+

/*    @Bean public WebClient productServiceWebClient () {

        return WebClient.builder()
                        .baseUrl (productServiceUrl)
                        .clientConnector (new ReactorClientHttpConnector (HttpClient.from (newTcpClient())))
                        .build();
    }*/

    @Bean public WebClient ourUserServiceWebClient () {

        return WebClient.builder()
                        .baseUrl (ourUserServiceUrl)
                        .clientConnector (new ReactorClientHttpConnector (HttpClient.from (newTcpClient())))
                        .build();
    }

    @Bean public WebClient orderServiceWebClient ()  {

        return WebClient.builder()
                        .baseUrl (orderServiceUrl)
                        .clientConnector (new ReactorClientHttpConnector (HttpClient.from (newTcpClient())))
                        .build();
    }

    private TcpClient newTcpClient (/*int connectionTimeOut, long readTimeOut, long writeTimeOut*/) {

        return TcpClient.create()
                        .option (ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000) //< таймаут на соединение
                        .doOnConnected (connection -> {
                            connection.addHandlerLast (new ReadTimeoutHandler (10000, TimeUnit.MILLISECONDS)); //< таймаут на чтение
                            connection.addHandlerLast (new WriteTimeoutHandler (5000, TimeUnit.MILLISECONDS)); //< таймаут на запись
                        });
    }
}
