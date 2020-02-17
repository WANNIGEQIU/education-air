package com.air.server.lecturer.Client;

import com.air.api.course.CourseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("heimao-course")
public interface LecturerFeign extends CourseApi {



}
