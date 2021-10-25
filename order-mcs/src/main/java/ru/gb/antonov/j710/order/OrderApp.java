package ru.gb.antonov.j710.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.order")
public class OrderApp
{
    public static void main (String[] args) { SpringApplication.run (OrderApp.class, args); }
}
