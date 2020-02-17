package com.air.server.user.exception;

import com.air.common.enums.ResultEnum;
import lombok.Data;

@Data
public class UserException extends RuntimeException {

    private Integer code;
    private String message;

    public UserException(Integer code, String message){
        this.message = message;
        this.code = code;
    }

    public UserException(ResultEnum resultEnum) {
         this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
    }


}
