package ru.gb.antonov.j710.productreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (scanBasePackages = "ru.gb.antonov.j710.productreview")
public class ProductReviewApp {

    public static void main (String[] args) {
        SpringApplication.run (ProductReviewApp.class, args);
    }
}
