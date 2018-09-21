package cn.corner.web.core.conf;

import cn.corner.web.core.authentication.mobile.SMSCodeAuthenticationFilter;
import cn.corner.web.core.authentication.mobile.SMSCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 短信登录的配置
 */
@Configuration
public class SMSCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private UserDetailsService detailsService;

    @Override
    public void configure(HttpSecurity http) {
        SMSCodeAuthenticationFilter authenticationFilter = new SMSCodeAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        SMSCodeAuthenticationProvider provider = new SMSCodeAuthenticationProvider();
        provider.setUserDetailsService(detailsService);

        http.authenticationProvider(provider)
            .addFilterAfter(authenticationFilter,UsernamePasswordAuthenticationFilter.class);
    }
}
