package com.zlkj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author sunny
 */
@SpringBootApplication
public class XzltProjectApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(XzltProjectApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(XzltProjectApplication.class);
    }
}
