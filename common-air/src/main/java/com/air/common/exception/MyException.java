package com.air.common.exception;

import com.air.common.enums.ResultEnum;
import lombok.Data;

@Data
public class MyException extends RuntimeException {

    private Integer code;
    private String message;

    public MyException(Integer code,String message){
        this.message = message;
        this.code = code;
    }

    public MyException(ResultEnum resultEnum) {
         this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
    }


}
