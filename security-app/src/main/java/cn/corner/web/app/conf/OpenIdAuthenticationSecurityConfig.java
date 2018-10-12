package cn.corner.web.app.conf;

import cn.corner.web.app.social.OpenIdAuthenticationFilter;
import cn.corner.web.app.social.OpenIdAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * app的social登录的配置,主要就是将我们处理这种登录方式的filter添加到过滤器链上去
 * 业务：用户已经进行了社交绑订
 */
@Configuration
public class OpenIdAuthenticationSecurityConfig extends AbstractHttpConfigurer<OpenIdAuthenticationSecurityConfig,HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SocialUserDetailsService detailsService;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Override
    public void configure(HttpSecurity http) {
        OpenIdAuthenticationFilter authenticationFilter = new OpenIdAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        OpenIdAuthenticationProvider provider = new OpenIdAuthenticationProvider();
        provider.setUserDetailsService(detailsService);
        provider.setUsersConnectionRepository(usersConnectionRepository);

        http.authenticationProvider(provider)
            .addFilterAfter(authenticationFilter,UsernamePasswordAuthenticationFilter.class);
    }
}
