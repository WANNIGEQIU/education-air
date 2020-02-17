package com.air.server.course.mapper;

import com.air.server.course.entity.EduSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;


@Mapper
public interface EduCategoryMapper extends BaseMapper<EduSubject> {
}
