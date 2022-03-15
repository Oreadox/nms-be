package com.news.nms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@MapperScan("com.news.nms.mapper")
public class NmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NmsApplication.class, args);
    }
}
