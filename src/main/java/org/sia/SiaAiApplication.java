package org.sia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@MapperScan(basePackages = "org.sia.mapper")
@SpringBootApplication(scanBasePackages = {"org.sia", "cn.hutool.extra.spring"})
@EnableScheduling
@EnableFeignClients
public class SiaAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SiaAiApplication.class, args);
    }

}
