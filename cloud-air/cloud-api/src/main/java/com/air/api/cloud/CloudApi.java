package com.air.api.cloud;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface CloudApi {


    /**
     * 腾讯云删除视频
     */
    @PostMapping("/videoup/media/{videoSourceId}")
     void DeleteMedia(@PathVariable("videoSourceId") String id);
}
