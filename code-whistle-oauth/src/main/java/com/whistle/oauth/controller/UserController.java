package com.whistle.oauth.controller;

import com.whistle.oauth.bean.dto.UserDTO;
import com.whistle.oauth.business.UserBiz;
import com.whistle.starter.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * @author Gentvel
 */
@Validated
@Api(tags = "用户模块")
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserBiz userBiz;


    @ApiParam(name = "username",value = "用户名",required = true)
    @ApiOperation(value = "用户名校验",notes = "校验当前是否存在传入的用户名")
    @GetMapping("exist/{username}")
    public Result<?> exists(@PathVariable @NotBlank(message = "参数不能为空！") String username){
        userBiz.exists(username);
        return Result.ok();
    }


    @ApiParam(name = "username",value = "用户名",required = true)
    @ApiOperation(value = "用户注册",notes = "用户新增")
    @PostMapping("signup")
    public Result<?> signup(@RequestBody @Validated UserDTO userDTO){
        userBiz.addUser(userDTO);
        return Result.ok();
    }





}
