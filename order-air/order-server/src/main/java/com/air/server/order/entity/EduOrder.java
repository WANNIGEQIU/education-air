package com.air.server.order.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EduOrder对象", description="")
public class EduOrder implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "数量")
    private Integer totalNum;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "支付类型 0 线上 1 其他")
    private Integer payType;

    @ApiModelProperty(value = "支付宝交易号")
    private String zfbNo;

    @ApiModelProperty(value = "下单用户")
    private String username;

    @ApiModelProperty(value = "订单状态 0 未完成 1 已完成 3 交易关闭")
    private Integer orderStatus;

    @ApiModelProperty(value = "支付状态 0 未支付 1 已支付")
    private Integer payStatus;

    @ApiModelProperty(value = "订单来源 0 web 1 app 2 微信小程序 3")
    private Integer sourceType;

    @ApiModelProperty(value = "1 已删除  0 未删除")
    @TableLogic         //逻辑删除注解
    private Integer did;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "课程名字")
    private String courseName;

    @ApiModelProperty(value = "课程id")
    private String courseId;


//    @ApiModelProperty(value = "课程金额")
//    private BigDecimal courseAmount;

    @ApiModelProperty(value = "优惠金额")
    @TableField("Preferential_amount")
    private BigDecimal preferentialAmount;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime eduCreate;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime eduModified;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;




}
