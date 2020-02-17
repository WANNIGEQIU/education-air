package com.air.server.course.mapper;

import com.air.server.course.entity.EduChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程章节 Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
public interface EduChapterMapper extends BaseMapper<EduChapter> {


    List<EduChapter> queryList(String id);
}
