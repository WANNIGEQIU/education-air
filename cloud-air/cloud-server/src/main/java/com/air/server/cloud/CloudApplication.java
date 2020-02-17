package com.air.server.cloud;


import com.air.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

@MySpringCloud
@Slf4j
public class CloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class,args);
        log.info("云OSS服务启动成功");
    }
}
