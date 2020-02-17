package com.air.server.cloud.service.Impl;


import com.air.common.util.LocalDateTimeUtils;
import com.air.server.cloud.config.ConstantProperties;
import com.air.server.cloud.entity.videoInfoBean;
import com.air.server.cloud.service.VideoService;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoService videoService;


    @Autowired
    private RabbitTemplate rabbitTemplate;



    @Override
    public Integer uploadTencentCloud(MultipartFile file,String id) throws IOException {
        log.info("视频id: {}",id);
        File path = null;
        String fileId = null;
        Map<String, Object> hashMap = new HashMap<>();

        try {

            String filename = file.getOriginalFilename();
            path = new File(
                    "/Users/haha/demo/" + filename);
            if (path.getParentFile().exists()) {
                path.delete();
            } else {
                path.mkdirs();
            }
            file.transferTo(path);


            VodUploadClient client = new VodUploadClient(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);
            VodUploadRequest request = new VodUploadRequest();
            //上传后的媒体名称，若不填默认采用 MediaFilePath 的文件名。
            filename = UUID.randomUUID().toString() + LocalDateTimeUtils.formatOther("yyyyMMdd") + filename;
            request.setMediaName("hello_" + filename);
            request.setMediaFilePath(path.getAbsolutePath());

            VodUploadResponse response = client.upload("ap-guangzhou", request);
            //视频凭证
             fileId = response.getFileId();
            if (!StringUtils.isEmpty(fileId)) {
                log.info("视频上传到腾讯云成功 {}", fileId);
                path.delete();
                // 发送mq
                Map<String, Object> map = new HashMap<>();
                map.put("proof",fileId);
                map.put("vid",id);
                this.rabbitTemplate.convertAndSend("VIDEO","video",map);
                log.info("发送云mq---> 凭证{}, 视频id{}",fileId,id);
                return 1;


            }


        } catch (Exception e) {
            // 业务方进行异常处理
            path.delete();
            log.error("视频上传到腾讯云失败", e.getMessage());
            return 0;

        }

        return 0;


    }

    @Override
    public void deleteMedia(String id) {
        Map<String, Object> map = new HashMap<>();
        try {

            Credential cred = new Credential(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);
            // String params = "{\"FileId\":\"5285890797884794546\"}";

            String params = "{\"FileId\":" + id + "}";
            log.info("视频id {}", params);
            DeleteMediaRequest req = DeleteMediaRequest.fromJsonString(params, DeleteMediaRequest.class);

            DeleteMediaResponse resp = client.DeleteMedia(req);


        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }


    }

    @Override
    public videoInfoBean queryVideInfo(String vid) {
        Float duration = 0.0f;
        Long size = 0l;


        try{

            Credential cred = new Credential(ConstantProperties.SECRETID, ConstantProperties.SECRETKEY);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vod.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            VodClient client = new VodClient(cred, "", clientProfile);
            String params = "{\"FileIds\":[ "+vid+"]}";
            DescribeMediaInfosRequest req = DescribeMediaInfosRequest.fromJsonString(params, DescribeMediaInfosRequest.class);

            DescribeMediaInfosResponse resp = client.DescribeMediaInfos(req);
            MediaInfo[] mediaInfoSet = resp.getMediaInfoSet();
            for (MediaInfo mediaInfo : mediaInfoSet) {
                MediaTranscodeInfo transcodeInfo = mediaInfo.getTranscodeInfo();
                MediaTranscodeItem[] transcodeSet = transcodeInfo.getTranscodeSet();
                for (MediaTranscodeItem transcodeItem : transcodeSet) {
                     duration = transcodeItem.getDuration();
                     size = transcodeItem.getSize();

                }
            }
            videoInfoBean infoBean = new videoInfoBean();
            infoBean.setSize(String.valueOf(size));
            infoBean.setDuration(String.valueOf(duration));
            return infoBean;


        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

        return null;

    }


}
