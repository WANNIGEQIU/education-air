package com.air.server.user.service;


import com.air.server.user.entity.EduUser;
import com.air.server.user.entity.dto.UserDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-21
 */
public interface EduUserService  {

    Integer queryNums(String day);

    Boolean check(String data, Integer type);


    void sendVerifyCode(String mobile);

    int UserRegister(UserDto eduUser, String verifyCode);

    EduUser queryUserIsexist(String account, String password);

    Map userLogin(String account, String password);

    String checkToken(String token);

    Page getUserList(Page<EduUser> objectPage, UserDto dto);

    boolean deleteUser(String id);

    boolean prohibitUser(String id);

    boolean recoveryUser(String id);

    Page<EduUser> deleteUserList(Page<EduUser> objectPage);

    EduUser getUserInfo(String username);

    Integer updatePoints(String username, String amount);

    void lossCode(String mobile);

    Integer lossPassword1(UserDto dto);

    boolean lossPassword2(String s1 ,String s2,String s3);
}
