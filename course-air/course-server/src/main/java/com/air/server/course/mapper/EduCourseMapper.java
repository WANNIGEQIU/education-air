package com.air.server.course.mapper;

import com.air.common.vo.CourseVo;
import com.air.server.course.entity.EduCourse;
import com.air.server.course.entity.EduUcourse;
import com.air.server.course.entity.dto.CourseCondtionDto;
import com.air.server.course.entity.dto.CourseInfoDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-14
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CourseInfoDto getCourseInfo(String id);

    Page<CourseCondtionDto> selectCondtion(Page<CourseCondtionDto> p, CourseInfoDto info);

    CourseInfoDto queryInfo(String id);

    List<CourseVo> selectCourse8Lid(String id);

    List<EduCourse> queryPopularCourse();

    int userIsBuy(String cid, String username);

    int addUcourse(Map map);

    List<EduUcourse> getMyCourse1(String username);

    Integer queryCourse(String day);

    Page<EduCourse> selectMyCourse(Page page, String username);
}


