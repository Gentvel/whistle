package com.whistle.oauth.business;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whistle.oauth.bean.RespEnum;
import com.whistle.oauth.bean.dto.UserDTO;
import com.whistle.oauth.entity.PriRbacUser;
import com.whistle.oauth.service.PriRbacUserService;
import com.whistle.starter.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        long count = priRbacUserService.count(new LambdaQueryWrapper<PriRbacUser>().eq(PriRbacUser::getUserName, username));
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
        if (!priRbacUserService.save(priRbacUser)) {
            throw new BizException(RespEnum.USER_ADD_ERROR);
        }
    }
}
