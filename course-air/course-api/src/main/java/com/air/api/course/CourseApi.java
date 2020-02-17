package com.air.api.course;


import com.air.common.vo.CourseVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface CourseApi {

    /**
     * 根据讲师id查询课程信息
     * @param
     * @return
     */
    @GetMapping("/course/queryids/{id}")
     List<CourseVo> queryIds(@PathVariable("id") String id);


    /**
     * 添加课程用户关联表
     * @param courseId username
     * @retrurn true ok
     */
    @PostMapping("/course/adduc/{courseId}/{username}")
     Boolean  addUcourse(@PathVariable("courseId") String courseId,
                         @PathVariable("username") String username);


    @GetMapping("/course/courseNum/{day}")
     Integer queryCourse(@PathVariable("day") String day);
}
