package com.air.server.order;


import com.air.common.annotation.MySpringCloud;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@MySpringCloud
@MapperScan("com.air.server.order.mapper")

public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
