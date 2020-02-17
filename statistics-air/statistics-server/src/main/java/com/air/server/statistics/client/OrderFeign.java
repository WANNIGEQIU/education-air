package com.air.server.statistics.client;

import com.air.api.order.OrderApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("heimao-order")
public interface OrderFeign extends OrderApi {
}
