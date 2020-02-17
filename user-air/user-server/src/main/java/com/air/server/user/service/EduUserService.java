package com.air.server.user.service;


import com.air.server.user.entity.EduUser;
import com.air.server.user.entity.dto.UserDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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

    int UserRegister(EduUser eduUser, String verifyCode);

    EduUser queryUserIsexist(String account, String password);

    String userLogin(String account, String password);

    String checkToken(String token);

    Page getUserList(Page<EduUser> objectPage, UserDto dto);

    boolean deleteUser(String id);

    boolean prohibitUser(String id);

    boolean recoveryUser(String id);

    Page<EduUser> deleteUserList(Page<EduUser> objectPage);

    EduUser getUserInfo(String username);

    Integer updatePoints(String username, String amount);

}
