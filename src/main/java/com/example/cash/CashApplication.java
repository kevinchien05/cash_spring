package com.example.cash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class CashApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // Loads the .env file
        System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
        System.setProperty("WEB_DOMAIN", dotenv.get("WEB_DOMAIN"));
        SpringApplication.run(CashApplication.class, args);
    }

}
