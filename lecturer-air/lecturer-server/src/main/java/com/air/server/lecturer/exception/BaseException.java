package com.air.server.lecturer.exception;


import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BaseException {

    //全局异常
    @ExceptionHandler(Exception.class)
    public ResultCommon baseExceptionHandler(Exception e){
        e.printStackTrace();
        System.out.println("全局异常处理");
        log.error(e.getMessage());
        return ResultCommon.setResult(ResultEnum.EXCEPTION_SERVER);
    }
    //针对某一异常
    @ExceptionHandler(ArithmeticException.class)
    public ResultCommon ArithmeticException(ArithmeticException e){
       // e.printStackTrace();
        log.error(e.getMessage());
        return ResultCommon.setResult(ResultEnum.ZERO_EXCEPTION);

    }

    //自定义异常

    @ExceptionHandler(LecturerException.class)
    public ResultCommon myException(LecturerException e){
        System.out.println("自定义异常处理");
        return ResultCommon.resultFail(e.getCode(),e.getMessage());

    }
}