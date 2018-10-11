package cn.corner.web.core.authorize;

import cn.corner.web.core.conf.SecurityConstant;
import cn.corner.web.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 用于设置各模块公用的拦截路径配置
 */
@Component
@Order(Integer.MIN_VALUE)
public class MyAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry) {
          urlRegistry.antMatchers(
                 SecurityConstant.VALIDATE_CODE_URL
                ,SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_MOBILE
                ,SecurityConstant.LOGIN_PAGE_URL
                ,securityProperties.getBrowser().getLoginPage()
                ,securityProperties.getBrowser().getSignUpUrl()
                ,securityProperties.getBrowser().getSession().getSessionInvalidUrl()
                ,securityProperties.getBrowser().getSignOutUrl())
                .permitAll();
    }
}
