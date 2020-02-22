package com.air.server.order;


import com.air.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@MySpringCloud
@MapperScan("com.air.server.order.mapper")
@Slf4j

public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
        log.info("order 服务 启动成功");
    }
}
