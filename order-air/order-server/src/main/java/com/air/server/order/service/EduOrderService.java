package com.air.server.order.service;

import com.air.server.order.entity.EduOrder;
import com.air.server.order.entity.dto.OrderDto;
import com.air.server.order.entity.vo.PayAsyncVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-10
 */
public interface EduOrderService {

    String saveOrder(OrderDto order);

    Integer paySuccess(PayAsyncVo payAsyncVo);

    Page<EduOrder> queryCondtion(Page<EduOrder> p, OrderDto dto);

    boolean deleteOrder(String id);

    Page getDelete(Page<EduOrder> objectPage);

    Page getMyOrder(Page p, String username);

    Integer queryOrderNum(String day);
}
