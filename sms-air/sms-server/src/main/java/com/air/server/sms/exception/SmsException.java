package com.air.server.sms.exception;

import com.air.common.enums.ResultEnum;
import lombok.Data;

@Data
public class SmsException extends RuntimeException {

    private Integer code;
    private String message;

    public SmsException(Integer code, String message){
        this.message = message;
        this.code = code;
    }

    public SmsException(ResultEnum resultEnum) {
         this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
    }


}
