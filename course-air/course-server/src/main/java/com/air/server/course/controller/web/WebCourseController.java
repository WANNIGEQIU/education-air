package com.air.server.course.controller.web;

import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.common.vo.CourseVo;
import com.air.server.course.entity.EduCourse;
import com.air.server.course.entity.dto.TwoSubjectDto;
import com.air.server.course.entity.dto.VideoDto;
import com.air.server.course.exception.CourseException;
import com.air.server.course.service.EduCategoryService;
import com.air.server.course.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course/web")

public class WebCourseController {
        @Autowired
        private EduCourseService courseService;

        @Autowired
        private EduCategoryService categoryService;

    /**
     * 课程分页列表
     * @param page
     * @param limit
     * @return
     */
    @PostMapping("/{page}/{limit}")
    public ResultCommon getCourseList(
            @PathVariable Long page, @PathVariable Long limit,
            @RequestBody(required = false) CourseVo bean) {
        Page<EduCourse> p = new Page<>(page, limit);
        Page courseList = courseService.getCourseList(p,bean);
        return ResultCommon.resultOk(courseList);

    }

    /**
     * 查询课程基本描述讲师分类章节小节
     * @param id 课程id
     * @return
     */
     @GetMapping("/details/{id}")
    public ResultCommon getCourseDetails(@PathVariable String id ) {
         Map courseDetails = courseService.getCourseDetails(id);
         return ResultCommon.resultOk(courseDetails);
     }

    /**
     * 查询一级类别下的二级类别和课程信息
     * @return
     */
    @GetMapping("/courseinfo/{page}/{limit}/{oneid}")
        public ResultCommon queryCourseInfo(
                @PathVariable Long page,
                @PathVariable Long limit,
                @PathVariable("oneid") String id){

        Page<EduCourse> returnPage = new Page<>(page, limit);
        Map map = courseService.queryCourseInfo(returnPage, id);
        return ResultCommon.resultOk(map);

    }

    /**
     * 查询一级二级类别名称 和二级类别下的课程信息
     * @param page
     * @param limit
     * @param id
     * @return
     */
    @GetMapping("/couseinfo2/{page}/{limit}/{twoid}")
    public ResultCommon queryCourseInfo1(
            @PathVariable Long page,
            @PathVariable Long limit,
            @PathVariable("twoid") String id) {
        Page<EduCourse> returnPage = new Page<>(page, limit);
        Map map = courseService.queryCourseInfo1(returnPage, id);
        return ResultCommon.resultOk(map);


    }

    /**
     * 查询热门课程
     * @return
     */
    @GetMapping("/Popular")
    public ResultCommon queryPopularCourse() {
        List<EduCourse> eduCourses = courseService.queryPopularCourse();
        if (eduCourses.size() >0) {
            return ResultCommon.resultOk(eduCourses);
        }else {
            return ResultCommon.resultFail(ResultEnum.NO_POPULAR_COURSE);
        }
    }

    /**
     *  获取课程小节视频id凭证
     * @param  id 视频id
     * @return  课程名称 视频播放权限
     */
    @GetMapping("/videoinfo/{id}")
    public ResultCommon queryVideoInfo(@PathVariable String id) {
        VideoDto videoDto = courseService.queryVideoInfo(id);
        return ResultCommon.resultOk(videoDto);
    }

    /**
     * 根据课程id 和用户名查询此用户是否购买
     */
    @GetMapping("/ucbuy/{cid}/{username}")
    public ResultCommon userIsBuy(@PathVariable String cid,
                                  @PathVariable String username){

        if (StringUtils.isEmpty(cid) && StringUtils.isEmpty(username)) {
            throw new CourseException(ResultEnum.PARAM_ERROR);
        }
        Boolean buy = this.courseService.userIsBuy(cid, username);
        if (buy) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultOk(false);
        }
    }


    /**
     * 获取我的课程信息
     * @param username
     * @return
     */
    @GetMapping("/mycourse/{username}")
    public ResultCommon getMyCourse(@PathVariable String username) {
        if (StringUtils.isEmpty(username)) {
            throw new CourseException(ResultEnum.PARAM_ERROR);
        }

        List myCourse = this.courseService.getMyCourse(username);

        if (CollectionUtils.isEmpty(myCourse)) {
            return ResultCommon.resultOk("你还没有购买的课程");
        }else {
            return ResultCommon.resultOk(myCourse);
        }

    }

    /**
     * 根据一级类别获取二级类别
     * @param firstId
     * @return
     */
    @GetMapping("/category/{firstId}")
    public ResultCommon getSecondListByFirsrId(@PathVariable String firstId) {
        List<TwoSubjectDto> list = this.courseService.getSecondListByFirsrId(firstId);
        return ResultCommon.resultOk(list);
    }

    /**
     * 获取课程
     */
    @GetMapping("/getcourse/{cid}")
    public ResultCommon getcourse(@PathVariable String cid) {
        CourseVo getcourse = this.courseService.getcourse(cid);
        return ResultCommon.resultOk(getcourse);
    }





}
