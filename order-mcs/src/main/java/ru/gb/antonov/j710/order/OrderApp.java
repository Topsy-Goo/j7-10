package ru.gb.antonov.j710.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.order")
@PropertySource ("secret.properties") //< сообщаем, что у приложения есть ещё один файл настроек
public class OrderApp {

    public static void main (String[] args) { SpringApplication.run (OrderApp.class, args); }
}
