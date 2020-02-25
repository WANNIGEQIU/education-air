package com.air.api.user;


import com.air.api.user.bean.UserBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    /**
     * feign
     * 获取指定日期 用户的注册人数
     ** @return
     */
    @GetMapping("/user/querynums/{day}")
    public Integer queryNums(@PathVariable("day") String day);


    /**
     * 查询用户是否存在 根据用户名或手机号和密码
     */
    @GetMapping("/user/isexist")
    public UserBean queryUserIsexist(@RequestParam String account, @RequestParam String password);


    /**
     * 更新用户积分
     * @param username  用户名字
     * @param amount 订单金额
     * @return
     */

    @GetMapping("/user/points/{username}/{amount}")
    public boolean updatePoints(@PathVariable("username") String username,
                                @PathVariable("amount") String amount);

    /**
     * 更新头像
     * @param path
     * @param username
     * @return
     */
    @PostMapping("/avatar/{path}/{username}")
     boolean upAvatar(
             @PathVariable("path") String path,
             @PathVariable("username") String username);
}
