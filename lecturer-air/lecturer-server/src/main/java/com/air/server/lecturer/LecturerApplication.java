package com.air.server.lecturer;

import com.air.common.annotation.MySpringCloud;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

@MySpringCloud
@Slf4j
@MapperScan("com.air.server.lecturer.mapper")
public class LecturerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LecturerApplication.class,args);
        log.info("***********LecturerApplication server is running ***********");
    }
}
