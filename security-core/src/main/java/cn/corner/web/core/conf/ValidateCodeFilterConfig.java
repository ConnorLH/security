package cn.corner.web.core.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

/**
 * 验证码拦截与校验过滤器配置
 * 这里只需要添加一个过滤器到security的过滤器链中即可
 */
@Configuration
public class ValidateCodeFilterConfig extends AbstractHttpConfigurer<ValidateCodeFilterConfig, HttpSecurity> {

    @Autowired
    @Qualifier("validateCodeFilter")
    private Filter validateCodeFilter;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
