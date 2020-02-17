package com.air.server.user.controller.admin;

import com.air.common.ResultCommon;
import com.air.common.bean.UserBean;
import com.air.common.enums.ResultEnum;
import com.air.server.user.entity.EduUser;
import com.air.server.user.entity.dto.UserDto;
import com.air.server.user.exception.UserException;
import com.air.server.user.service.EduUserService;
import com.air.server.user.utils.MD5Utils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EduUserService userService;



    @PostMapping("/login")
    public ResultCommon login(@RequestBody UserBean bean, HttpServletRequest request,
                              HttpServletResponse response) {
        System.out.println(bean.getUsername()+"===="+bean.getPassword());
        if ("admin".equals(bean.getUsername()) && "123456".equals(bean.getPassword())) {
            Cookie cookie = new Cookie("demo", MD5Utils.saltRandom());
            cookie.setDomain("localhost");
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60);
            response.addCookie(cookie);
            return ResultCommon.resultOk(cookie.getValue());
        }else {
            return ResultCommon.resultFail("登录失败");
        }

    }

    /**
     * 获取用户列表
     * @param page
     * @param limit
     * @param dto
     * @return
     *
     */
    @PostMapping("/user/{page}/{limit}")
    public ResultCommon getUserList(
                                    @PathVariable Long page,
                                    @PathVariable Long limit,
                                    @RequestBody(required = false) UserDto dto) {

        if (page < 0 || limit < 0) {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }

        Page<EduUser> objectPage = new Page<>(page, limit);
        Page userList = this.userService.getUserList(objectPage, dto);
         if (userList == null) {
             return ResultCommon.resultFail();
         }else {
             return ResultCommon.resultOk(userList);
         }
         }

    /**
     * 删除用户
     */
    @DeleteMapping("/user/{id}")
    public ResultCommon deleteUser(@PathVariable String id) {

        if (StringUtils.isEmpty(id)) {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }
        boolean b = this.userService.deleteUser(id);
        if (b){
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail(false);
        }
    }


    /**
     * 禁用用户
     */
    @PutMapping("/prohibit/{id}")
    public ResultCommon prohibitUser(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }
        boolean b = this.userService.prohibitUser(id);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail(false);
        }

    }

    /**
     * 恢复被禁用用户
     */

    @PutMapping("/recovery/{id}")
    public ResultCommon recoveryUser(@PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }
        boolean b = this.userService.recoveryUser(id);
        if (b) {
            return ResultCommon.resultOk(true);
        }else {
            return ResultCommon.resultFail(false);
        }

    }

    /**
     * 已被删除用户列表
     */
    @PostMapping("/dellis/{page}/{limit}")
  public ResultCommon deleteUserList(@PathVariable long page,
                                     @PathVariable long limit) {
        if (page < 0 || limit < 0) {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }
        Page<EduUser> page1 = new Page<>(page, limit);
        Page<EduUser> eduUserPage = this.userService.deleteUserList(page1);
        return ResultCommon.resultOk(eduUserPage);

    }












}
