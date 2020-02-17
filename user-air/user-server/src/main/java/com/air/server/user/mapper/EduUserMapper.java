package com.air.server.user.mapper;


import com.air.server.user.entity.EduUser;
import com.air.server.user.entity.dto.UserDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-01-21
 */
public interface EduUserMapper extends BaseMapper<EduUser> {

    Integer queryNums(String day);

    Page getUserList(Page<EduUser> objectPage, UserDto dto);

    Page<EduUser> deleteUserList(Page<EduUser> objectPage);
}
