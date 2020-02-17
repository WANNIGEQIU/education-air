package com.air.server.order.controller.web;


import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.server.order.entity.EduOrder;
import com.air.server.order.entity.dto.OrderDto;
import com.air.server.order.entity.vo.PayAsyncVo;
import com.air.server.order.exception.OrderException;
import com.air.server.order.service.EduOrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器 web
 * </p>
 *
 * @author 玩你个球儿
 * @since
 */
@RestController
@RequestMapping("/order")
public class EduOrderController {

    @Autowired
    private EduOrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 生成订单
     */
    @PostMapping("/saveorder")
     public ResultCommon saveOrder(@RequestBody OrderDto order) {


        String s = this.orderService.saveOrder(order);
        if ("error".equals(s)) {
            return ResultCommon.resultFail("支付失败发生了错误");
        }else {
            return ResultCommon.resultOk(s);
        }

    }

    /**
     * 支付回调 更新支付状态
     */
    @PostMapping("/payok")
    public ResultCommon paySuccess(PayAsyncVo payAsyncVo) {

        Integer integer = this.orderService.paySuccess(payAsyncVo);
        if (integer == 1) {
            return ResultCommon.resultOk(integer);
        }else {
            return ResultCommon.resultFail(integer);
        }
    }

    /**
     * 我的订单
     * @param username
     * @return
     */
    @GetMapping("/myorder/{username}/{page}/{limit}")
    public ResultCommon getMyOrder(@PathVariable String username,
                                   @PathVariable long page,
                                   @PathVariable long limit) {

        if (StringUtils.isEmpty(username)) {
            throw new OrderException(ResultEnum.PARAM_ERROR);
        }
        if (page <0 || limit <0) {
            page = 0L;
            limit = 10L;
        }
        Page<EduOrder> p = new Page<>(page, limit);
        Page myOrder = this.orderService.getMyOrder(p, username);
        return ResultCommon.resultOk(myOrder);

    }


}

