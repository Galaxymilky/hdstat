package com.hdstat;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class HdstatApplication {
    public static void main(String[] args) {
        SpringApplication.run(HdstatApplication.class, args);
    }
}
