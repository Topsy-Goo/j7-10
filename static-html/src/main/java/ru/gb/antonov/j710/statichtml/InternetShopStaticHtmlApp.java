package ru.gb.antonov.j710.statichtml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.statichtml")
public class InternetShopStaticHtmlApp
{
    public static void main (String[] args)
    {
        SpringApplication.run (InternetShopStaticHtmlApp.class, args);
    }
}
