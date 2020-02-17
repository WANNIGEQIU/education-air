package com.air.server.user.controller.feign;


import com.air.server.user.entity.EduUser;
import com.air.server.user.exception.UserException;
import com.air.server.user.service.EduUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserFeignController {
    @Autowired
    private EduUserService userService;

    /**
     * feign
     * 获取指定日期 用户的注册人数
     ** @return
     */
    @GetMapping("/querynums/{day}")
    public Integer queryNums(@PathVariable String day) {

        Integer integer = userService.queryNums(day);
        return  integer == null? 0 : integer;
    }

    /**
     * 查询用户是否存在 根据用户名或手机号和密码
     * 内部使用
     */
    @GetMapping("/isexist")
    public EduUser queryUserIsexist(@RequestParam String account, @RequestParam String password) {
        EduUser b = userService.queryUserIsexist(account, password);
                return b;

    }

    /**
     * 更新用户积分
     * @param username  用户名字
     * @param amount 订单金额
     * @return
     */

    @GetMapping("/points/{username}/{amount}")
    public boolean updatePoints(@PathVariable String username,
                         @PathVariable String amount) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(amount)) {
            throw new UserException(98323,"参数不对");
        }
        Integer integer = this.userService.updatePoints(username, amount);
        return integer == 1 && integer !=null? true:false;

    }

}
