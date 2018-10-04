package cn.corner.web.conf;

import cn.corner.web.core.conf.LoginConfig;
import cn.corner.web.core.conf.SMSCodeAuthenticationSecurityConfig;
import cn.corner.web.core.conf.SecurityConstant;
import cn.corner.web.core.conf.ValidateCodeFilterConfig;
import cn.corner.web.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 浏览器security的总配置，其中应用了核心配置（登录、验证码过滤器、手机验证码登录拦截）
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("myUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SMSCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeFilterConfig validateCodeFilterConfig;

    @Autowired
    @Qualifier("loginCoreConfig")
    private LoginConfig loginConfig;

    /**
     *  注册Social登录需要的过滤器
     */
    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        loginConfig.applyLoginConfigure(http);
        http
            .apply(validateCodeFilterConfig)
            .and()
            .apply(smsCodeAuthenticationSecurityConfig)
            .and()
            .apply(springSocialConfigurer)
            .and()
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    .userDetailsService(userDetailsService)
                    .and()
                .sessionManagement()
                    .invalidSessionStrategy(invalidSessionStrategy)
                    .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                    .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                    .expiredSessionStrategy(sessionInformationExpiredStrategy)
                    .and()
                    .and()
                .authorizeRequests()
                    .antMatchers(
                            SecurityConstant.VALIDATE_CODE_URL
                            ,SecurityConstant.LOGIN_PAGE_URL
                            ,securityProperties.getBrowser().getLoginPage()
                            ,securityProperties.getBrowser().getSignUpUrl()
                            ,"/session/invalid","/user/regist")
                            .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .csrf().disable();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
       // repository.setCreateTableOnStartup(true);
        return repository;
    }

}
