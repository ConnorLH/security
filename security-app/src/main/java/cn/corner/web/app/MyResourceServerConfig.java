package cn.corner.web.app;

import cn.corner.web.core.conf.SMSCodeAuthenticationSecurityConfig;
import cn.corner.web.core.conf.SecurityConstant;
import cn.corner.web.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableResourceServer
public class MyResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     *  注册Social登录需要的过滤器
     */
    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    @Autowired
    private SMSCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .formLogin()
                // 需要登录时让其重定向到这个url
                .loginPage(SecurityConstant.LOGIN_PAGE_URL)
                .loginProcessingUrl(SecurityConstant.LOGIN_PROCESS_URL)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
           /* .apply(validateCodeFilterConfig)
            .and()*/
            .apply(smsCodeAuthenticationSecurityConfig)
            .and()
            .apply(springSocialConfigurer)
            .and()
            .authorizeRequests()
                .antMatchers(
                    SecurityConstant.VALIDATE_CODE_URL
                    ,SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_MOBILE
                    ,SecurityConstant.LOGIN_PAGE_URL
                    ,securityProperties.getBrowser().getLoginPage()
                    ,securityProperties.getBrowser().getSignUpUrl()
                    ,securityProperties.getBrowser().getSession().getSessionInvalidUrl()
                    ,securityProperties.getBrowser().getSignOutUrl()
                    ,"/user/regist")
                     .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
            .csrf().disable();
    }
}
