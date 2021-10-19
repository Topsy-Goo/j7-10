package ru.gb.antonov.j710.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.order")
public class InternetShopOrderApplication
{
    public static void main (String[] args)
    {
        SpringApplication.run (InternetShopOrderApplication.class, args);
    }
}
