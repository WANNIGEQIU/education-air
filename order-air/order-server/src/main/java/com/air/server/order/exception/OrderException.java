package com.air.server.order.exception;

import com.air.common.enums.ResultEnum;
import lombok.Data;

@Data
public class OrderException extends RuntimeException {

    private Integer code;
    private String message;

    public OrderException(Integer code, String message){
        this.message = message;
        this.code = code;
    }

    public OrderException(ResultEnum resultEnum) {
         this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
    }


}
