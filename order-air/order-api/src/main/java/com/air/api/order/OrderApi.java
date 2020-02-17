package com.air.api.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


public interface OrderApi {


    @GetMapping("/feign/queryOrderNum/{day}")
    public Integer queryOrderNum(@PathVariable("day") String day);
}
