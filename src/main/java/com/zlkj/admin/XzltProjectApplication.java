package com.zlkj.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.zlkj.admin")
public class XzltProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(XzltProjectApplication.class, args);
    }

}
