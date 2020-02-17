package com.air.server.order.controller;

import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.server.order.entity.EduOrder;
import com.air.server.order.entity.dto.OrderDto;
import com.air.server.order.exception.OrderException;
import com.air.server.order.service.EduOrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderAdminController {

    @Autowired
    private EduOrderService orderService;

    /**
     * 订单分页条件查询
     *
     * @param page  页数
     * @param limit 每页条数
     * @param dto
     * @return
     */
    @PostMapping("/query/{page}/{limit}")
    public ResultCommon queryCondtion(@PathVariable Long page, @PathVariable Long limit,
                                      @RequestBody(required = false) OrderDto dto) {

        if (page < 0 || limit < 0) {
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        Page<EduOrder> p = new Page<>(page, limit);
        Page<EduOrder> orderPage = this.orderService.queryCondtion(p, dto);


        return ResultCommon.resultOk(orderPage);

    }

    /**
     * 已删除订单
     */
    @GetMapping("/delete/{page}/{limit}")
    public ResultCommon getDelete(@PathVariable Long page,@PathVariable Long limit) {
        if (page < 0 || limit < 0) {
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        Page<EduOrder> objectPage = new Page<>(page, limit);
        Page delete = this.orderService.getDelete(objectPage);
        return ResultCommon.resultOk(delete);
    }

    /**
     *  删除订单
     */
        @DeleteMapping("/delete/{id}")
    public ResultCommon deleteOrder(@PathVariable String id) {
        boolean b = this.orderService.deleteOrder(id);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultOk(false);
        }
    }
}
