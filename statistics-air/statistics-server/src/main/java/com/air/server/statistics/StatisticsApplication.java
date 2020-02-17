package com.air.server.statistics;


import com.air.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@MySpringCloud
@Slf4j
@MapperScan("com.air.server.statistics.mapper")
public class StatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class,args);
        log.info("统计管理服务启动成功");
    }
}
