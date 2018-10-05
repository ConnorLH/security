package cn.corner.web.app.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 主要用于设置AuthorizationServer的EndPoint的权限拦截控制,特别是"/oauth/authorize"
 * 因为这个EndPoint需要登录后才能正常执行（需要获取给予授权的用户，即谁给第三方客户端授权的）
 * 所以必须在这里对其进行登录拦截
 *
 */
@Configuration
public class APPSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/oauth/authorize").authenticated().and()
                .authorizeRequests().anyRequest().permitAll().and().httpBasic();
    }
}
