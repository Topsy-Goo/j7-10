package ru.gb.antonov.j710.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.users")
public class OurUsersApp {

    public static void main (String[] args) {
        SpringApplication.run (OurUsersApp.class, args);
    }
}
