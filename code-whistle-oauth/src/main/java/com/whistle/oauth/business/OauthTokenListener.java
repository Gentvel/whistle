package com.whistle.oauth.business;

import cn.dev33.satoken.listener.SaTokenListenerForLog;
import cn.dev33.satoken.stp.SaLoginModel;
import org.springframework.stereotype.Component;

/**
 * @author Gentvel
 */
@Component
public class OauthTokenListener extends SaTokenListenerForLog {
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        super.doLogin(loginType, loginId, tokenValue, loginModel);
    }
}
