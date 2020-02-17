package com.air.common;


import com.air.common.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResultCommon<T> implements Serializable {
    private static final long serialVersionUID = 100L;

    private boolean flag;    // 是否成功
    private Integer code;    //状态码
    private String message; //返回消息
    private T data;         //返回数据


    private ResultCommon(){};

    // 成功
    public static <T> ResultCommon resultOk(T data){
        ResultCommon<T> r = new ResultCommon<>();
        r.setFlag(true);
        r.setCode(ResultEnum.SUCCESS.getCode());
        r.setMessage(ResultEnum.SUCCESS.getMessage());
        r.setData(data);
        return r;
    }
    public static <T> ResultCommon resultOk(){
        ResultCommon<T> r = new ResultCommon<>();
        r.setFlag(true);
        r.setMessage(ResultEnum.SUCCESS.getMessage());
        r.setCode(ResultEnum.SUCCESS.getCode());
        return  r;
    }
    //失败的
    public static <T> ResultCommon  resultFail(){
        ResultCommon r = new ResultCommon();
        r.setFlag(false);
        r.setCode(ResultEnum.ERROR.getCode());
        r.setMessage(ResultEnum.ERROR.getMessage());
        return  r;
    }

    public static <T> ResultCommon  resultFail(T data){
        ResultCommon r = new ResultCommon();
        r.setFlag(false);
        r.setCode(ResultEnum.ERROR.getCode());
        r.setMessage(ResultEnum.ERROR.getMessage());
        r.setData(data);
        return  r;
    }
    public static <T> ResultCommon  resultFail(String message,T data){
        ResultCommon r = new ResultCommon();
        r.setFlag(false);
        r.setCode(ResultEnum.ERROR.getCode());
        r.setMessage(message);
        r.setData(data);
        return  r;
    }

    public static <T> ResultCommon  resultFail(Integer code,String message){
        ResultCommon r = new ResultCommon();
        r.setFlag(false);
        r.setCode(code);
        r.setMessage(message);
        return  r;
    }

    public static <T> ResultCommon setResult(ResultEnum result){
        ResultCommon r = new ResultCommon();
        r.setCode(result.getCode());
        r.setFlag(false);
        r.setMessage(result.getMessage());
        return r;
    }








}
