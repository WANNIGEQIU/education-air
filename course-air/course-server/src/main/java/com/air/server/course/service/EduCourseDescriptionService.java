package com.air.server.course.service;


import com.air.server.course.entity.EduCourseDescription;
import com.air.server.course.entity.dto.CourseInfoDto;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
public interface EduCourseDescriptionService  {

    /**
     * 添加课程描述信息
     */
    public Boolean insertCourseDesc(CourseInfoDto desc) ;

    Boolean uploadDesc(EduCourseDescription description);

    Boolean descDelete(String id);

}
