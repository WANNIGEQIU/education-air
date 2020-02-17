package com.air.server.statistics.client;

import com.air.api.course.CourseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("heimao-course")
public interface CourseFeign extends CourseApi {
}
