package com.retailsense;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.retailsense")
public class RetailSenseApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetailSenseApplication.class, args);
    }
}