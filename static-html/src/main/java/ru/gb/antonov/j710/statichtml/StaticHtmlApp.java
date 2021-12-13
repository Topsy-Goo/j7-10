package ru.gb.antonov.j710.statichtml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.statichtml")
public class StaticHtmlApp {

    public static void main (String[] args) {
        SpringApplication.run (StaticHtmlApp.class, args);
    }
}
