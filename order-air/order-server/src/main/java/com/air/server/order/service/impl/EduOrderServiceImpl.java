package com.air.server.order.service.impl;

import com.air.common.enums.ResultEnum;
import com.air.common.util.LocalDateTimeUtils;
import com.air.server.order.client.CourseFeign;
import com.air.server.order.client.UserFeignClient;
import com.air.server.order.config.AliPayTemplate;
import com.air.server.order.entity.EduOrder;
import com.air.server.order.entity.dto.OrderDto;
import com.air.server.order.entity.vo.PayAsyncVo;
import com.air.server.order.entity.vo.PayVo;
import com.air.server.order.exception.OrderException;
import com.air.server.order.mapper.EduOrderMapper;
import com.air.server.order.service.EduOrderService;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-10
 */
@Service
@Slf4j
public class EduOrderServiceImpl implements EduOrderService {

    @Autowired
    private EduOrderMapper orderMapper;

    @Autowired
    private AliPayTemplate aliPayTemplate;

    @Autowired
    private CourseFeign  courseFeign;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserFeignClient userFeignClient;




    @Override
    public String saveOrder(OrderDto order) {
        EduOrder eduOrder = new EduOrder();
        BeanUtils.copyProperties(order, eduOrder);
        //线上支付
         eduOrder.setPayType(0);
         //订单状态 未完成
        eduOrder.setOrderStatus(0);
        // 支付状态 未支付
        eduOrder.setPayStatus(0);
        //订单来源 web
        eduOrder.setSourceType(0);
        // 优惠金额
        eduOrder.setPreferentialAmount(BigDecimal.valueOf(0));
        int insert = this.orderMapper.insert(eduOrder);
        PayVo payVo = new PayVo();
        payVo.setOut_trade_no(eduOrder.getId());
        payVo.setTotal_amount(eduOrder.getTotalAmount().toString());
        payVo.setSubject(eduOrder.getCourseName());
        try {
            String pay = this.aliPayTemplate.pay(payVo);
            return pay;
        } catch (AlipayApiException e) {
            e.printStackTrace();

        }

        return "error";


    }

    @Override
    public Integer paySuccess(PayAsyncVo payAsyncVo) {
        HashMap<String, Object> hashMap = new HashMap<>();

        if ("TRADE_SUCCESS".equals(payAsyncVo.getTrade_status())) {
            String zfbNo = payAsyncVo.getTrade_no(); // 支付宝交易号
            String orderNo = payAsyncVo.getOut_trade_no(); // 订单号
            String patTime = payAsyncVo.getGmt_payment(); // 交易付款时间
            String receipt_amount = payAsyncVo.getReceipt_amount(); // 商家实收金额
            String pay_amount = payAsyncVo.getBuyer_pay_amount(); // 用户实际付款金额


            LocalDateTime time = LocalDateTimeUtils.string2parse(patTime, "yyyy-MM-dd HH:mm:ss");

            QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
            EduOrder eduOrder = this.orderMapper.selectById(orderNo);
            if (eduOrder == null) {
                return 0;
            }
            // 更新状态
            hashMap.put("order_status", 1);
            hashMap.put("pay_status", 1);
            hashMap.put("pay_time", time);
            hashMap.put("order_no", orderNo);
            hashMap.put("zfb_no", zfbNo);
            hashMap.put("pay_money",pay_amount);
            int i = this.orderMapper.paySuccess(hashMap);
            if (i > 0) {
                log.info("更新订单状态成功");
                // 添加用户课程关联信息
                String courseId = eduOrder.getCourseId();
                String username = eduOrder.getUsername();
                String amount = eduOrder.getTotalAmount().toString();// 订单金额

                Boolean r = this.courseFeign.addUn(courseId, username);
                if (r) {
                    //更新用户积分
                    this.userFeignClient.updatePoints(username, amount);

                    // 发mq更新课程购买数量
                    this.rabbitTemplate.convertAndSend("ORDER", "order", courseId);
                    log.info("发送ORDER-mq : {}", courseId);
                    return 1;
                } else {
                    return 0;
                }


            } else {
                throw new OrderException(32932, "订单状态更新异常");

            }


        } else {
            return 0;
        }


    }

    @Override
    public Page<EduOrder> queryCondtion(Page<EduOrder> p, OrderDto dto) {

        log.info("订单分页查询 页数: {}, 条数: {},条件: {}", p.getPages() + 1, p.getSize(), dto);
        Page<EduOrder> orderPage = this.orderMapper.queryCondtion(p, dto);
        if (orderPage == null) {
            throw new OrderException(ResultEnum.QUERY_ERROR);
        }
        ;

        return orderPage;
    }

    @Override
    public boolean deleteOrder(String id) {
        int i = this.orderMapper.deleteById(id);

        return i > 0 ? true : false;
    }

    @Override
    public Page getDelete(Page<EduOrder> objectPage) {
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        Page<EduOrder> eduOrderPage = this.orderMapper.getDelete(objectPage);
        return eduOrderPage;
    }

    @Override
    public Page getMyOrder(Page p,String username) {
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        Page page = this.orderMapper.selectPage(p, wrapper);
        return page;
    }

    @Override
    public Integer queryOrderNum(String day) {
        Integer integer = this.orderMapper.queryOrderNum(day);
        return integer == null ? -1:integer;
    }
}
