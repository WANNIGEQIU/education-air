package com.air.server.course.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VideoDto {

    private String id;

    @ApiModelProperty(value = "小节名称")
    private String title;
    private Integer sort;

    @ApiModelProperty(value = "课程名")
    private String courseName;

    @ApiModelProperty(value = "是否免费")
    private Boolean isFree;

    @ApiModelProperty(value = "视频名称")
    private String videoName;
    @ApiModelProperty(value = "视频id")
    private String videoSourceId;

}
