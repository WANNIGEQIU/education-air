package com.air.server.course;


import com.air.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;

@MySpringCloud
@Slf4j


public class CourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class,args);
        log.info("课程微服务启动成功 ");
    }
}
