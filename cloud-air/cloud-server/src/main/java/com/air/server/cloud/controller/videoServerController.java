package com.air.server.cloud.controller;


import com.air.common.ResultCommon;
import com.air.server.cloud.entity.videoInfoBean;
import com.air.server.cloud.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/videoup")
public class videoServerController {

    @Autowired
    private VideoService videoService;

    /**
     * 腾讯云上传视频 发送mq更新课程里的视频凭证
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public ResultCommon uploadTencentCloud (@RequestParam("file") MultipartFile file,
                                            @RequestParam String id) throws IOException {
        Integer fileId = videoService.uploadTencentCloud(file,id);

        if (fileId == 1) {
            return ResultCommon.resultOk("视频已在后台上传");
        }else {
            return ResultCommon.resultFail("视频上传失败");
        }
    }

    /**
     * 腾讯云删除视频
     */
    @PostMapping("/media/{videoSourceId}")
    public ResultCommon DeleteMedia(@PathVariable("videoSourceId")String id) {

         videoService.deleteMedia(id);
        return ResultCommon.resultOk("腾讯云视频删除成功");

    }

    /**
     * 查询腾讯云视频信息内容
     * @param vid 视频id凭证
     * @return  视频大小 size 视频时长 duration
     */
    @PostMapping("/queryvideo/{vid}")
    public ResultCommon queryVideInfo(@PathVariable String vid) {
        videoInfoBean videoInfoBean = videoService.queryVideInfo(vid);

        return ResultCommon.resultOk(videoInfoBean);
    }

}
