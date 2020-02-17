package com.air.server.order.mapper;

import com.air.server.order.entity.EduOrder;
import com.air.server.order.entity.dto.OrderDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-10
 */
public interface EduOrderMapper extends BaseMapper<EduOrder> {

    int paySuccess(Map map);

    Page<EduOrder> queryCondtion(Page<EduOrder> p, OrderDto dto);

    Page<EduOrder> getDelete(Page<EduOrder> objectPage);

    Integer queryOrderNum(String day);
}
