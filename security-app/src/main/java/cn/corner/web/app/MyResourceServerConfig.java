package cn.corner.web.app;

import cn.corner.web.app.conf.OpenIdAuthenticationSecurityConfig;
import cn.corner.web.core.authorize.AuthorizeConfigManager;
import cn.corner.web.core.conf.SMSCodeAuthenticationSecurityConfig;
import cn.corner.web.core.conf.SecurityConstant;
import cn.corner.web.core.conf.ValidateCodeFilterConfig;
import cn.corner.web.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 资源服务器配置
 * 有了这个资源服务器的注解就相当于使用了另一套WebSecurity的支持，这个支持OAuth2的认证授权处理
 * 所以在app这种架构，例如微服务之间的交流（不涉及和用户直接交流）就非常适用这种方式，因为OAuth2的认证对于此种场景很合适
 * 当然用户使用这种方式和服务交流也可以
 */
@SuppressWarnings("ALL")
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
    private ValidateCodeFilterConfig validateCodeFilterConfig;

    @Autowired
    private SMSCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Autowired
    private ApplicationContext context;

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
            .apply(validateCodeFilterConfig)
                .and()
            .apply(smsCodeAuthenticationSecurityConfig)
                .and()
            .apply(springSocialConfigurer)
                .and()
            .apply(openIdAuthenticationSecurityConfig)
                .and()
            .csrf().disable();
        authorizeConfigManager.config(http.authorizeRequests());
    }

    /**
     * 为什么配置这个请参见
     * https://stackoverflow.com/questions/29328124/no-bean-resolver-registered-in-the-context-to-resolve-access-to-bean
     * https://blog.csdn.net/liu_zhaoming/article/details/79411021
     * https://github.com/spring-projects/spring-security-oauth/issues/730
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.expressionHandler(getOAuth2MethodSecurityExpressionHandler());
    }

    @Bean
    public DefaultWebSecurityExpressionHandler getOAuth2MethodSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setApplicationContext(context);
        return defaultWebSecurityExpressionHandler;
    }



}
