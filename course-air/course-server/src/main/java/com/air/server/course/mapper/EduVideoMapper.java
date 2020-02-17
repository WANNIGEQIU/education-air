package com.air.server.course.mapper;

import com.air.server.course.entity.EduVideo;
import com.air.server.course.entity.dto.VideoDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
public interface EduVideoMapper extends BaseMapper<EduVideo> {

    Integer seleteids(String vsearch);

    List<EduVideo> queryList(String id);

    VideoDto queryVideoInfo(String id);
}
