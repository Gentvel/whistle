package com.whistle.oauth.business;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whistle.oauth.bean.RespEnum;
import com.whistle.oauth.bean.dto.UserDTO;
import com.whistle.oauth.entity.PriRbacUser;
import com.whistle.oauth.service.PriRbacUserService;
import com.whistle.starter.enums.DataEnum;
import com.whistle.starter.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Gentvel
 */
@Service
public class UserBiz {

    @Resource
    private PriRbacUserService priRbacUserService;

    /**
     * 校验当前是否存在该用户名
     *
     * @param username 用户名
     */
    public void exists(String username) {
        long count = priRbacUserService.count(new LambdaQueryWrapper<PriRbacUser>()
                .eq(PriRbacUser::getUserName, username)
                .eq(PriRbacUser::getStatus, DataEnum.STATUS_ENABLE.getValue())
        );
        if (count > 0) {
            throw new BizException(RespEnum.USER_EXISTS);
        }
    }


    /**
     * 新增用户
     *
     * @param userDTO 用户
     */
    public void addUser(UserDTO userDTO) {
        PriRbacUser priRbacUser = BeanUtil.copyProperties(userDTO, PriRbacUser.class);
        priRbacUser.setPassword(SaSecureUtil.sha256(priRbacUser.getPassword()));
        priRbacUser.setStatus(DataEnum.STATUS_ENABLE.getValue());
        priRbacUser.setIsAdmin(DataEnum.NO_ADMIN.getValue());
        if (!priRbacUserService.save(priRbacUser)) {
            throw new BizException(RespEnum.USER_ADD_ERROR);
        }
    }

    public void login(UserDTO userDTO) {
        PriRbacUser user = priRbacUserService.getOne(new LambdaQueryWrapper<PriRbacUser>().eq(PriRbacUser::getUserName, userDTO.getUserName()));
        if (Objects.isNull(user)) {
            throw new BizException(RespEnum.USER_LOGIN_ERROR);
        }
        if(!SaSecureUtil.sha256(userDTO.getPassword()).equalsIgnoreCase(user.getPassword())){
            throw new BizException(RespEnum.USER_PASSWORD_ERROR);
        }
        if(user.getStatus().equals(DataEnum.STATUS_DISABLE.getValue())){
            throw new BizException(RespEnum.USER_STATUS_ERROR);
        }
        SaLoginModel saLoginModel = SaLoginConfig.setExtra("name", "zhangsan");
        StpUtil.login(user.getUid(),saLoginModel);
    }
}
