package com.air.server.course.exception;

import com.air.common.enums.ResultEnum;
import lombok.Data;

@Data
public class CourseException extends RuntimeException {

    private Integer code;
    private String message;

    public CourseException(Integer code, String message){
        this.message = message;
        this.code = code;
    }

    public CourseException(ResultEnum resultEnum) {
         this.message = resultEnum.getMessage();
        this.code = resultEnum.getCode();
    }


}
