package com.air.server.course.controller;


import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.common.vo.CourseVo;
import com.air.server.course.entity.dto.CourseCondtionDto;
import com.air.server.course.entity.dto.CourseInfoDto;
import com.air.server.course.exception.CourseException;
import com.air.server.course.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
@RestController
@RequestMapping("/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    /**
     * 根据课程id查询讲师课程基本和描述分类信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public ResultCommon queryInfo(@PathVariable String id) {
        CourseInfoDto courseInfoDto = courseService.queryInfo(id);
        return ResultCommon.resultOk(courseInfoDto);
    }



    /**
     * 添加课程信息
     * @return
     */
    @PostMapping("/save")
    public ResultCommon saveCouseInfo(@RequestBody CourseInfoDto info) {
        if (StringUtils.isEmpty(info.getTitle())) {
            throw new CourseException(ResultEnum.NO_COURSE_TITLE);
        }
        if (StringUtils.isEmpty(info.getLecturerId())) {
            throw new CourseException(ResultEnum.NO_COURSE_LECTURER);
        }
        if(StringUtils.isEmpty(info.getCategoryId())) {
            throw new CourseException(ResultEnum.NO_COURSE_CATEGORY);
        }
        String s = courseService.saveCouseInfo(info);
        return ResultCommon.resultOk(s);

    }

    /**
     * 查询课程信息和描述信息
     * @param id
     * @return
     */
     @GetMapping("/courseInfo/{id}")
     public ResultCommon getCourseInfo(@PathVariable("id") String id) {
         CourseInfoDto courseInfoDto = courseService.getCourseInfo(id);
         return ResultCommon.resultOk(courseInfoDto);
     }


    /**
     * 课程基本和课程描述更新
     * @param info
     * @return
     */
    @PutMapping("/upload")
     public ResultCommon upload(@RequestBody CourseInfoDto info) {
        Boolean r = this.courseService.uploadCourse(info);
        if (r) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }

    /**
     * 课程分页
     * @return
     */
    @PostMapping("/condtion/{page}/{limit}")
    public ResultCommon courseCondtion(@PathVariable( "page") Long page,
                                       @PathVariable("limit") Long limit,
                                       @RequestBody(required = false) CourseInfoDto info){
        Page<CourseCondtionDto> p = new Page<>(page, limit);
        Page r = courseService.selectCondtion(p, info);

        return ResultCommon.resultOk(r);
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{id}")
    public ResultCommon courseDelete(@PathVariable("id") String id) {
        Boolean delete = courseService.courseDelete(id);
        if (delete) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }

    }


    @PutMapping("/state/{id}")
    public ResultCommon statePut(@PathVariable String id) {
        Boolean put = courseService.statePut(id);
        if (put) {
            return ResultCommon.resultOk(put);
        }else {
            return ResultCommon.resultFail(put);
        }
    }


    @PutMapping("/change/{id}/{status}")
    public ResultCommon changeStatus(@PathVariable String id,@PathVariable String status) {
        Boolean r = courseService.changeStatus(id, status);
        if (r) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }


    /**
     * 根据cid 查询订单所需要的信息内容
     * @param cid 课程id
     * @return
     */
    @GetMapping("/loadorder/{cid}")
    public ResultCommon getOrderInfo(@PathVariable String cid) {
        if (StringUtils.isEmpty(cid)){
            throw new CourseException(ResultEnum.PARAM_ERROR);
        }
        CourseVo orderInfo = this.courseService.getOrderInfo(cid);
        return ResultCommon.resultOk(orderInfo);
    }














}

