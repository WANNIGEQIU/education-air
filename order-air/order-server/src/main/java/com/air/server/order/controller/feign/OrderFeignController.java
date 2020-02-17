package com.air.server.order.controller.feign;


import com.air.common.util.LocalDateTimeUtils;
import com.air.server.order.service.EduOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign")
public class OrderFeignController {

    @Autowired
    private EduOrderService orderService;

    @GetMapping("/queryOrderNum/{day}")
    public Integer queryOrderNum(@PathVariable String day) {
        if (StringUtils.isEmpty(day)) {
            day = LocalDateTimeUtils.formatOther("yyyy-MM-dd");
        }
        Integer integer = this.orderService.queryOrderNum(day);

        return integer;


    }
}
