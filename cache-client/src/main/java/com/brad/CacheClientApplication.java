package com.brad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CacheClientApplication
 */
@SpringBootApplication(scanBasePackages = {"com.brad"})
//@EnableDiscoveryClient
public class CacheClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheClientApplication.class, args);
        System.out.println("启动成功");
    }
}
