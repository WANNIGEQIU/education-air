package com.air.server.course.service;


import com.air.common.vo.CourseVo;
import com.air.server.course.entity.EduCourse;
import com.air.server.course.entity.dto.CourseInfoDto;
import com.air.server.course.entity.dto.TwoSubjectDto;
import com.air.server.course.entity.dto.VideoDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
public interface EduCourseService  {
    /**
     * 根据讲师id查询课程信息
     * @param
     * @return
     */
    List<CourseVo> selectCouse8Lid(String id);

    String saveCouseInfo(CourseInfoDto info);

    List a();

    CourseInfoDto getCourseInfo(String id);

    Boolean uploadCourse(CourseInfoDto info);

    Page selectCondtion(Page p, CourseInfoDto info);

    Boolean courseDelete(String id);

    CourseInfoDto queryInfo(String id);

    Boolean statePut(String id);


    Boolean changeStatus(String id, String status);

    Page getCourseList(Page<EduCourse> p,CourseVo bean);

    Map getCourseDetails(String id);

    Map queryCourseInfo(Page<EduCourse> returnPage, String id);

    Map queryCourseInfo1(Page<EduCourse> returnPage, String id);

    List<EduCourse> queryPopularCourse();

    VideoDto queryVideoInfo(String id);

    Boolean userIsBuy(String cid, String username);

    CourseVo getOrderInfo(String cid);

    Boolean addUcourse(String courseId, String username);

    List getMyCourse(String username);

    Integer queryCourse(String day);

   List<TwoSubjectDto> getSecondListByFirsrId(String firstId);

    CourseVo getcourse(String cid);
}

