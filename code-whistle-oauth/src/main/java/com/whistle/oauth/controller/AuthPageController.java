package com.whistle.oauth.controller;

import cn.hutool.core.util.RandomUtil;
import com.whistle.oauth.business.UserBiz;
import com.whistle.starter.boot.WhistleCommandRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author Gentvel
 */
@Controller
public class AuthPageController {

    @Resource
    private UserBiz userBiz;

//    @ApiOperation(value = "用户登录",notes = "用户登录")
//    @PostMapping("login")
//    public Result<?> login(@RequestBody @Validated UserDTO userDTO){
//        userBiz.login(userDTO);
//        return Result.ok();
//    }
//

    @RequestMapping(value = "login")
    public ModelAndView greeting(ModelAndView mv) {
        mv.setViewName("index");
        mv.addObject("title","欢迎使用Thymeleaf!");
        mv.addObject("num", RandomUtil.randomString("1234",1));
        return mv;
    }
}
