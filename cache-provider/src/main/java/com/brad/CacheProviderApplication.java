package com.brad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CacheProviderApplication
 */
@SpringBootApplication(scanBasePackages = {"com.brad", "com.brad.service"})
public class CacheProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheProviderApplication.class, args);
        System.out.println("启动成功");
    }
}
