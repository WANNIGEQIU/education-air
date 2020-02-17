package com.air.server.user.controller;


import com.air.common.ResultCommon;
import com.air.common.enums.ResultEnum;
import com.air.server.user.entity.EduUser;
import com.air.server.user.exception.UserException;
import com.air.server.user.service.EduUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
     * 注册功能 data 1 注册成功 0 注册失败
     * @param eduUser
     * @param verifyCode
     * @return
     */
    @PostMapping("/register")
    public ResultCommon UserRegister(EduUser eduUser, @RequestParam("verifyCode") String verifyCode) {
         if (StringUtils.isEmpty(eduUser.getUsername())) {
             throw new UserException(ResultEnum.USER_NO_USERNAME);
         }
         if (StringUtils.isEmpty(eduUser.getPassword())) {
             throw new UserException(ResultEnum.USER_NO_PASSWORD);
         }
         if(StringUtils.isEmpty(eduUser.getMobile())) {
             throw new UserException(ResultEnum.USER_NO_PHONE);
         }
        int i = this.userService.UserRegister(eduUser, verifyCode);
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


        String token = this.userService.userLogin(account, password);

            if (!StringUtils.isEmpty(token)) {
                return ResultCommon.resultOk(token);
            }else {
                return ResultCommon.resultFail();
            }

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




}

