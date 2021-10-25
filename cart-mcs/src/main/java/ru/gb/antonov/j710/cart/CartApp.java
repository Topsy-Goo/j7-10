package ru.gb.antonov.j710.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.cart")
public class CartApp
{
    @Autowired private Environment env;

    @PostConstruct private void init () { ru.gb.antonov.j710.cart.CartFactory.init(env); }

    public static void main (String[] args) { SpringApplication.run (CartApp.class, args); }
}

