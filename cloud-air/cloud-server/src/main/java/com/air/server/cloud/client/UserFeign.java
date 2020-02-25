package com.air.server.cloud.client;

import com.air.api.user.UserApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient("heimao-user")
@Component
public interface UserFeign extends UserApi {
}
