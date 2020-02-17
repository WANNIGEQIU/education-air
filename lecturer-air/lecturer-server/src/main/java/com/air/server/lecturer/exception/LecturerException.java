package com.air.server.lecturer.exception;

import com.air.common.enums.ResultEnum;
import lombok.Data;

@Data
public class LecturerException extends RuntimeException {

    private Integer code;
    private String message;

    public LecturerException(Integer code, String message){
        this.message = message;
        this.code = code;
    }

    public LecturerException(ResultEnum resultEnum) {
         this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
    }


}
