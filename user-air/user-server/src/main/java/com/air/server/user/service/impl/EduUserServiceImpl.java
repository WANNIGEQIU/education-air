package com.air.server.user.service.impl;

import com.air.common.enums.ResultEnum;
import com.air.common.exception.MyException;
import com.air.common.util.VerifyCodeUtils;
import com.air.server.user.entity.EduRole;
import com.air.server.user.entity.EduUser;
import com.air.server.user.entity.dto.UserDto;
import com.air.server.user.exception.UserException;
import com.air.server.user.mapper.EduRoleMapper;
import com.air.server.user.mapper.EduUserMapper;
import com.air.server.user.service.EduUserService;
import com.air.server.user.utils.JwtUtil;
import com.air.server.user.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 玩你个球儿
 * @since
 */
@Service
@Transactional
@Slf4j
public class EduUserServiceImpl  implements EduUserService {

    @Autowired
    private EduUserMapper userMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private EduRoleMapper roleMapper;

    @Override
    public Integer queryNums(String day) {


        return  userMapper.queryNums(day);
    }

    @Override
    public Boolean check(String data, Integer type) {

        EduUser eduUser = new EduUser();
        if (type == 1) {
            QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
            wrapper.eq("username",data);
            return  userMapper.selectCount(wrapper) == 0;

        }else if (type ==2 ) {
            QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
            wrapper.eq("mobile",data);
            return  userMapper.selectCount(wrapper) == 0;
        }else {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }



    }



    @Override
    public void sendVerifyCode(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            throw new UserException(ResultEnum.PARAM_ERROR);
        }
        // 验证码
        String verifyCode = VerifyCodeUtils.getVerifyCode();

        // 发消息给 sms 的mq
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile",mobile);
        hashMap.put("verifyCode",verifyCode);
        this.rabbitTemplate.convertAndSend("heimaoSms","sms",hashMap);
            log.info("发送到mq: {},{}",mobile,verifyCode);
        // 验证存到redis
        this.redisTemplate.opsForValue().set("user"+mobile,verifyCode,5, TimeUnit.MINUTES);




    }

    @Override

    public int UserRegister(UserDto eduUser, String verifyCode) {

        // 获取redis 存的验证码 key (user + mobile)
        String code = this.redisTemplate.opsForValue().get("user" + eduUser.getMobile());
        if ((verifyCode.equals(code)) == false ) {
            throw new UserException(ResultEnum.VERFYICODE_IS_ERROR);
        }
        // 是否存在用户
        QueryWrapper<EduUser> queryWrapper = new QueryWrapper<>();
          queryWrapper.eq("username",eduUser.getUsername());
        QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", eduUser.getMobile());
        EduUser one = this.userMapper.selectOne(queryWrapper);
        EduUser eduUser1 = this.userMapper.selectOne(wrapper);
        if (one != null) {
            throw new UserException(ResultEnum.USER_IS_EXIST);
        }
        if (eduUser1 != null) {
                throw new UserException(ResultEnum.USER_PHONE_ISEXIST);
            }
        EduUser user = new EduUser();
        BeanUtils.copyProperties(eduUser,user);


        log.info("用户账号密码: {},{}",eduUser.getUsername(),eduUser.getPassword());
        // 获取盐
        String salt = MD5Utils.saltRandom();
        user.setSalt(salt);
        // 密码加密
        String md5Password = MD5Utils.MD5EncodeUtf8(eduUser.getPassword(), salt);
        user.setPassword(md5Password);
        int insert = this.userMapper.insert(user);
        // 获取插入的id
        return insert > 0 ? insert : 0;


    }

    @Override
    public EduUser queryUserIsexist(String account, String password) {
         if (StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
             log.info("账号或密码为空");
             throw new MyException(ResultEnum.USER_EROR_LOGIN);
         }
        boolean integer = isInteger(account.substring(0, 1));
        // 如果数字开头 true为手机号否则用户名
         if (integer) {
             QueryWrapper<EduRole> wrapper = new QueryWrapper<>();
                wrapper.eq("phone",account);
             EduRole eduRole = roleMapper.selectOne(wrapper);
             if (eduRole == null) {
                 log.info("此用户不存在");
                 throw new MyException(12212,"用户不存在");

             }
             String salt = eduRole.getSalt();
             String s = MD5Utils.MD5EncodeUtf8(password, salt);
             QueryWrapper<EduUser> wrapper1 = new QueryWrapper<>();
             wrapper1.eq("mobile",account);
             wrapper1.eq("password",s);
             EduUser one = userMapper.selectOne(wrapper1);
             if (one == null) {
                 throw new MyException(12213,"查询用户失败");
             }
             EduUser user = new EduUser();
             BeanUtils.copyProperties(one,user);
             return user;

         }else {
             QueryWrapper<EduRole> wrapper = new QueryWrapper<>();
             wrapper.eq("username",account);
             EduRole eduRole = roleMapper.selectOne(wrapper);
             if (eduRole == null) {
                 log.info("此用户不存在");
                 throw new MyException(12212,"用户不存在");
             }
             String salt = eduRole.getSalt();
             String s = MD5Utils.MD5EncodeUtf8(password, salt);
             QueryWrapper<EduUser> queryWrapper = new QueryWrapper<>();
             queryWrapper.eq("username",account);
             queryWrapper.eq("password",s);
             EduUser eduUser = userMapper.selectOne(queryWrapper);
             if (eduUser == null) {
                 throw new MyException(12213,"查询用户失败");
             }
             EduUser user = new EduUser();
              BeanUtils.copyProperties(eduUser,user);
              return user;
         }

    }

    @Override
    public Map userLogin(String account, String password) {
        Map<String, Object> map = new HashMap<>();
        // true 手机号登录
        if (isInteger(account)) {
            QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
            wrapper.eq("mobile",account);
            EduUser eduUser = this.userMapper.selectOne(wrapper);
            if (eduUser == null) {
                throw new UserException(ResultEnum.USER_NOT_EXIST);
            }else if (eduUser.getProhibit() == 1) {
                throw new UserException(ResultEnum.USER_IS_PROHIBIT);
            }else {
                //比较密码
                String salt = eduUser.getSalt();
                if (MD5Utils.MD5EncodeUtf8(password,salt).equals(eduUser.getPassword())) {
                    // 生成令牌
                    String tokenSecret = JwtUtil.getTokenSecret(eduUser);
                    map.put("token",tokenSecret);
                    map.put("username",eduUser.getUsername());
                    map.put("phone",eduUser.getMobile());
                    return map;
                }else {
                    throw new UserException(ResultEnum.USER_EROR_LOGIN);
                }

            }

        }else {
            //用户名登录
            QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
            wrapper.eq("username",account);

            EduUser eduUser = this.userMapper.selectOne(wrapper);
            if (eduUser == null) {
                throw new UserException(ResultEnum.USER_NOT_EXIST);
            } else  if (eduUser.getProhibit() == 1) {
                throw new UserException(ResultEnum.USER_IS_PROHIBIT);
            }else {
                String salt = eduUser.getSalt();
                if (MD5Utils.MD5EncodeUtf8(password,salt).equals(eduUser.getPassword())) {
                    String tokenSecret = JwtUtil.getTokenSecret(eduUser);
                    map.put("token",tokenSecret);
                    map.put("username",eduUser.getUsername());
                    map.put("phone",eduUser.getMobile());
                    return map;
                }else {
                    throw new UserException(ResultEnum.USER_EROR_LOGIN);
                }
            }


        }


    }

    @Override
    public String checkToken(String token) {
        Claims claims = JwtUtil.checkJwt(token);
        String username =(String) claims.get("username");
        if (StringUtils.isEmpty(username)) {
            throw new UserException(9983,"token解析失败");
        }
        return username;

    }

    @Override
    public Page getUserList(Page<EduUser> objectPage, UserDto dto) {
        QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
        if (dto != null)  {
            if (!StringUtils.isEmpty(dto.getUsername())) {
                wrapper.eq("username",dto.getUsername());
            }
            if (!StringUtils.isEmpty(dto.getBeginTime())) {
                wrapper.ge("edu_create",dto.getBeginTime());
            }
            if (!StringUtils.isEmpty(dto.getEndTime())) {
                wrapper.le("edu_create",dto.getEndTime());
            }
            if (!StringUtils.isEmpty(dto.getProhibit())) {
                wrapper.eq("prohibit",dto.getProhibit());
            }
        }


        Page<EduUser> eduUserPage = this.userMapper.selectPage(objectPage, wrapper);


        return eduUserPage;
    }

    @Override
    public boolean deleteUser(String id) {
        int i = this.userMapper.deleteById(id);
        return i>0?true:false ;
    }

    @Override
    public boolean prohibitUser(String id) {
        EduUser eduUser = new EduUser();
        eduUser.setId(id);
        eduUser.setProhibit(1);
        int i = this.userMapper.updateById(eduUser);
        return i>0 ? true: false;
    }

    @Override
    public boolean recoveryUser(String id) {
        EduUser eduUser = new EduUser();
        eduUser.setId(id);
        eduUser.setProhibit(0);
        int i = this.userMapper.updateById(eduUser);

        return i>0?true: false;
    }

    @Override
    public Page<EduUser> deleteUserList(Page<EduUser> objectPage) {
        Page<EduUser> page = this.userMapper.deleteUserList(objectPage);

        return page;
    }

    @Override
    public EduUser getUserInfo(String username) {
        QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        EduUser eduUser = this.userMapper.selectOne(wrapper);
        if (eduUser == null) {
            throw new UserException(ResultEnum.QUERY_ERROR);
        }else {
            return eduUser;
        }
    }

    @Override
    public Integer updatePoints(String username, String amount) {

        QueryWrapper<EduUser> query = new QueryWrapper<>();
        query.eq("username",username);
        EduUser eduUser = this.userMapper.selectOne(query);
        Integer i1 = Integer.valueOf(eduUser.getPoints());
        Integer i2 = Integer.valueOf(amount.substring(0, amount.indexOf(".")));
        Integer i3 = i1 + i2;
        String resultPoint = i3.toString();
        eduUser.setPoints(resultPoint);
        int update = this.userMapper.update(eduUser, query);
        return update;


    }

    @Override
    public void lossCode(String mobile) {
        //此手机号是否注册
        QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        wrapper.eq("prohibit",0);
        Integer integer = this.userMapper.selectCount(wrapper);
        if (integer == 0) {
            throw new UserException(9833,"该手机号没有注册，请注册");
        }
        //验证码
        String verifyCode = VerifyCodeUtils.getVerifyCode();
        //发送mq
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("mobile",mobile);
        hashMap.put("verifyCode",verifyCode);
        this.rabbitTemplate.convertAndSend("loss","loss",hashMap);

        //验证码存入redis
        this.redisTemplate.opsForValue().set("loss"+mobile,verifyCode,5,TimeUnit.MINUTES);


    }

    @Override
    public Integer lossPassword1(UserDto dto) {

        String code = this.redisTemplate.opsForValue().get("loss" + dto.getMobile());
            if (StringUtils.isEmpty(code)) {
                throw new  UserException(ResultEnum.VERFYRCODE_IS_NULL);
            }
            if (code.equals(dto.getVerifyCode())) {
                return 1;
            }else {

                throw new UserException(ResultEnum.VERFYICODE_IS_ERROR);
            }



    }

    @Override
    public boolean lossPassword2(String s1, String s2,String s3) {
        if (s1.equals(s2)) {
            EduUser user = new EduUser();
            QueryWrapper<EduUser> wrapper = new QueryWrapper<>();
            wrapper.eq("mobile",s3);
            String salt = MD5Utils.saltRandom();
            String password = MD5Utils.MD5EncodeUtf8(s1, salt);
            user.setSalt(salt);
            user.setPassword(password);
            int update = this.userMapper.update(user, wrapper);
            return update > 0 ?true: false;
        }else {
            throw new UserException(9820,"两次输入的密码不一致");
        }

    }


    /*
    判断字符串是否为数字
     */
    public static boolean isInteger(String string) {
        Pattern compile = Pattern.compile("^[-\\+]?[\\d]*$");
        return compile.matcher(string).matches();
    }



}
