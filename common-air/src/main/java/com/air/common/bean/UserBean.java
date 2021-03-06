package com.air.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserBean {

    private String id;


    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "账户名")
    private String username;

    @ApiModelProperty(value = "性别 1 女 2 男")
    private Integer sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "盐")
    private String salt;

    @ApiModelProperty(value = "禁用状态 0 (false) 未禁用 1 禁用 ")
    private Integer prohibit;

    @ApiModelProperty(value = "0 未删除 1 删除")

    private Boolean deleted;


}
