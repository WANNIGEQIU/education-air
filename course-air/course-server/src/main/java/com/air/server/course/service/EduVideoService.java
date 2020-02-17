package com.air.server.course.service;


import com.air.server.course.entity.EduVideo;
import com.air.server.course.entity.dto.VideoDto;

import java.util.Map;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
public interface EduVideoService  {

    Boolean saveVideo(EduVideo eduVideo);

    Map queryOne(String id);

    Boolean updateVideo(VideoDto videoDto);

    Boolean deleteVideo(String id);
}
