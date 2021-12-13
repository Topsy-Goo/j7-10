package ru.gb.antonov.j710.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.gateway")
public class GatewayApp {

    public static final String AUTHORIZATION_HDR_TITLE = "Authorization";
    public static final String INAPP_HDR_LOGIN   = "username";
    public static final String INAPP_HDR_ROLES   = "roles";
    public static final String JWT_PAYLOAD_ROLES = "roles";
    public static final String BEARER_ = "Bearer ";

    public static void main (String[] args) {   SpringApplication.run (GatewayApp.class, args);   }
}
