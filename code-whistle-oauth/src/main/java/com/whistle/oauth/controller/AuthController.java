package com.whistle.oauth.controller;

import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.whistle.oauth.bean.dto.UserDTO;
import com.whistle.oauth.business.UserBiz;
import com.whistle.starter.boot.WhistleCommandRunner;
import com.whistle.starter.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Gentvel
 */
@Api(tags = "用户鉴权模块")
@RestController
@RequestMapping("auth")
public class AuthController {



    @ApiOperation(value = "登录地址 ",notes = "返回SSO认证中心登录地址")
    @GetMapping("getAuthUrl")
    public Result<?> getAuthUrl(String backUrl) {
        String serverAuthUrl = SaSsoUtil.buildServerAuthUrl(WhistleCommandRunner.getLocalAddress()+"/login", backUrl);
        return Result.ok(serverAuthUrl);
    }

    @ApiOperation(value = "用户是否登录",notes = "用户是否登录")
    @GetMapping("isLogin")
    public Result<?> isLogin() {
        return Result.ok(StpUtil.isLogin());
    }


}
