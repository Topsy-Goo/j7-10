package ru.gb.antonov.j710.monolith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.monolith.beans")
public class InternetShopApplication {

    @Autowired private Environment env;

    @PostConstruct private void init() { ru.gb.antonov.j710.monolith.Factory.init (env); }

    public static void main (String[] args) {
      SpringApplication.run (InternetShopApplication.class, args);
    }
}
