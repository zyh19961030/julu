package com.qu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.UnknownHostException;

/**
 * 启动类
 *
 * @author yanrunjie 2021-03-18
 */
@Slf4j
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
    }
}