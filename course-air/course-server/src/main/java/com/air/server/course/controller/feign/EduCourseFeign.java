package com.air.server.course.controller.feign;


import com.air.common.enums.ResultEnum;
import com.air.common.util.LocalDateTimeUtils;
import com.air.common.vo.CourseVo;
import com.air.server.course.exception.CourseException;
import com.air.server.course.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class EduCourseFeign {

    @Autowired
    private EduCourseService courseService;


    /**
     *
     *      根据讲师id查询课程信息 web feign讲师
     *
     */
    @GetMapping("/queryids/{id}")
    public List<CourseVo> queryIds (@PathVariable String id) {
        List<CourseVo> courseVos = courseService.selectCouse8Lid(id);
        return courseVos;
    }


    /**
     * 添加课程用户关联表
     * @param courseId username
     * @retrurn true ok
     */
    @PostMapping("/save/{courseId}/{username}")
    public Boolean addUn(@PathVariable String courseId, @PathVariable String username) {
        if (StringUtils.isEmpty(courseId) && StringUtils.isEmpty(username)) {
            throw new CourseException(ResultEnum.PARAM_ERROR);
        }

        Boolean result = this.courseService.addUcourse(courseId, username);
        if (true) {
            return result;
        }else {
            return !result;
        }
    }
    @PostMapping("/aa/{aa}")
    public Integer aa(@PathVariable("aa") String aa) {
        if (aa.equals("tom")) {
            return 1;
        }else {
            return 0;
        }
    }

    @GetMapping("/courseNum/{day}")
    public Integer queryCourse(@PathVariable String day) {

        if (StringUtils.isEmpty(day)) {
            day = LocalDateTimeUtils.formatOther("yyyy-MM-dd");
        }

        Integer integer = this.courseService.queryCourse(day);
        return integer;
    }




}
