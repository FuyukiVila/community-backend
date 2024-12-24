package com.fuyuki.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@MapperScan("com.fuyuki.backend.mapper")
@EnableCaching
public class BackendApplication extends SpringBootServletInitializer{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BackendApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
