package com.air.server.cloud.service;


import com.air.server.cloud.entity.videoInfoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {
    /**
     * 返回fileId
     * @param file
     * @return
     */
    Integer uploadTencentCloud(MultipartFile file, String id) throws IOException;

    void deleteMedia(String id);

    videoInfoBean queryVideInfo(String vid);
}

