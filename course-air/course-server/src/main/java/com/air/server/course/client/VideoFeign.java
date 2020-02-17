package com.air.server.course.client;

import com.air.api.cloud.CloudApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("heimao-cloud")
public interface VideoFeign  extends CloudApi {




}
