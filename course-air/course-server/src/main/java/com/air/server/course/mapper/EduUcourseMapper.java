package com.air.server.course.mapper;

import com.air.server.course.entity.EduCourse;
import com.air.server.course.entity.EduUcourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface EduUcourseMapper extends BaseMapper<EduUcourse> {
    Page<EduCourse> selectMyCourse(Page page, String username);
}
