package com.air.server.user.controller;


import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.server.user.entity.EduUser;
import com.air.server.user.entity.dto.UserDto;
import com.air.server.user.exception.UserException;
import com.air.server.user.service.EduUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 玩你个球儿
 *
 */
@RestController
@RequestMapping("/user")
public class EduUserController {
    @Autowired
    private EduUserService userService;




    /**
     * 检验用户名 或手机号是否唯一
     * @param data
     * @param type  1 用户名 2 手机号
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResultCommon check(@PathVariable String data, @PathVariable Integer type) {
        Boolean check = userService.check(data, type);
        return ResultCommon.resultOk(check);
    }

    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    @PostMapping("/verifycode/{mobile}")
    public ResultCommon sendVerifyCode(@PathVariable String mobile) {

        userService.sendVerifyCode(mobile);
        return ResultCommon.resultOk();

    }

    /**
     * 忘记密码 短信验证
     */
        @PostMapping("/lossCode/{mobile}")
        public ResultCommon lossCode(@PathVariable String mobile) {
            if (StringUtils.isEmpty(mobile)) {
                throw new UserException(4394,"手机号不能为空");
            }
             this.userService.lossCode(mobile);
            return ResultCommon.resultOk();
        }

    /**
     * 忘记密码 确认验证码手机号
     */
        @PostMapping("/lossPassword1")
        public ResultCommon lossPassword1(@RequestBody UserDto dto) {

            if (StringUtils.isEmpty(dto.getMobile())) {
                throw new UserException(3291,"手机号不能为空");
            }
            if (StringUtils.isEmpty(dto.getVerifyCode())) {
                throw new UserException(8233,"验证码不能为空");
            }
            Integer integer = this.userService.lossPassword1(dto);
            return ResultCommon.resultOk(integer);

        }
    /**
     * 忘记密码 修改密码
     */
    @PostMapping("/lossPassword2")
    public ResultCommon lossPassword2(@RequestBody UserDto dto) {
        if (StringUtils.isEmpty(dto.getMobile())) {
            throw new UserException(3291,"手机号不能为空");
        }
        if (StringUtils.isEmpty(dto.getPassword())) {
            throw new UserException(3294,"密码不能为null");
        }
        if (StringUtils.isEmpty(dto.getCheckPassword())) {
            throw new UserException(8223,"请再次输入密码");
        }
            String pass1 = dto.getPassword();
            String pass2 = dto.getCheckPassword();
            String phone = dto.getMobile();

        boolean b = this.userService.lossPassword2(pass1, pass2,phone);
        return ResultCommon.resultOk(b);


    }


    /**
     * 注册功能 data 1 注册成功 0 注册失败
     * @param bean
     * @param
     * @return
     */
    @PostMapping("/register")
    public ResultCommon UserRegister(UserDto bean) {
        String verfyCode;
         if (StringUtils.isEmpty(bean.getUsername())) {
             throw new UserException(ResultEnum.USER_NO_USERNAME);
         }
         if (StringUtils.isEmpty(bean.getPassword())) {
             throw new UserException(ResultEnum.USER_NO_PASSWORD);
         }
         if(StringUtils.isEmpty(bean.getMobile())) {
             throw new UserException(ResultEnum.USER_NO_PHONE);
         }
         if (StringUtils.isEmpty(bean.getVerifyCode())) {
             throw new UserException(32329,"验证码为空");
         }
          verfyCode = bean.getVerifyCode();
        int i = this.userService.UserRegister(bean,verfyCode);
        if (i == 1 ) {
             return ResultCommon.resultOk(i);
         }else {
             return ResultCommon.resultFail(i);
         }

    }

    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    @PostMapping("/login/{account}/{password}")
    public ResultCommon userLogin(@PathVariable String account, @PathVariable String password) {
            if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
                throw new  UserException(ResultEnum.PARAM_ERROR);
            }

        Map map = this.userService.userLogin(account, password);

        return ResultCommon.resultOk(map);

    }

    /**
     * 根据token获取用户名
     * @param token
     * @return
     */
    @GetMapping("/check/{token}")
    public ResultCommon checkToken(@PathVariable String token) {

        if (StringUtils.isEmpty(token)) {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }
        String s = this.userService.checkToken(token);
        return ResultCommon.resultOk(s);
    }


    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/info/{username}")
    public ResultCommon getUserInfo(@PathVariable String username) {
        if (StringUtils.isEmpty(username)) {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }
        EduUser userInfo = this.userService.getUserInfo(username);
        return ResultCommon.resultOk(userInfo);
    }


    /**
     * 个人信息修改
     */
    @PostMapping("/updateUser")
    public ResultCommon updateUser(@RequestBody UserDto dto){


        boolean b = this.userService.updateUser(dto);
        return ResultCommon.resultOk(b);

    }



}

