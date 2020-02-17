package com.air.server.user;


import com.air.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@Slf4j
@MySpringCloud
@MapperScan("com.air.server.user.mapper")

public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
        log.info("用户微服务启动成功");
    }
}
