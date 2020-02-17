package com.air.server.statistics.client;

import com.air.api.user.UserApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient("heimao-user")
public interface UserFeign extends UserApi {



}
