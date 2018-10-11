package cn.corner.web.app.conf;

import cn.corner.web.core.authorize.AuthorizeConfigProvider;
import cn.corner.web.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 自定义添加拦截路径配置
 */
//@Component
//@Order(1)
public class APPAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry) {
        try {
            urlRegistry
                    .antMatchers("/oauth/authorize").authenticated().and().httpBasic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
