package com.openclassrooms.paymybuddy;

import com.openclassrooms.paymybuddy.service.impl.UserAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class PayMyBuddyApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
    }
}
