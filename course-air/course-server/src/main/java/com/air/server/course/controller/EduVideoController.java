package com.air.server.course.controller;


import com.air.common.ResultCommon;
import com.air.server.course.entity.EduVideo;
import com.air.server.course.entity.dto.VideoDto;
import com.air.server.course.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-16
 */
@RestController
@RequestMapping("/eduvideo")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;



    /**
     *
     * @param eduVideo
     * @return
     */
    @PostMapping("/save")
    public ResultCommon saveVideo(@RequestBody EduVideo eduVideo) {

        Boolean result = videoService.saveVideo(eduVideo);
       if (result) {
           return ResultCommon.resultOk(result);
       }else {
           return ResultCommon.resultFail(result);
       }
    }

    /**
     *
     * @param id
     * @return title id sort,isFree
     */
    @GetMapping("/query/{vid}")
    public ResultCommon queryOne(@PathVariable("vid")String id) {
        Map map = this.videoService.queryOne(id);
        if (map.size() == 0) {
            map.put("result","写着玩");
            return ResultCommon.resultFail(map);
        } else {
            return ResultCommon.resultOk(map);
        }
    }

    /**
     *
     */
    @PutMapping("/update")
    public ResultCommon updateVideo(@RequestBody VideoDto videoDto) {
        Boolean r = videoService.updateVideo(videoDto);
        if (r) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }

    @DeleteMapping("/{id}")
    public ResultCommon deleteVideo(@PathVariable("id") String id) {
        Boolean r = videoService.deleteVideo(id);
        if (r) {
            return ResultCommon.resultOk();
        }else {
            return ResultCommon.resultFail();
        }
    }



}

