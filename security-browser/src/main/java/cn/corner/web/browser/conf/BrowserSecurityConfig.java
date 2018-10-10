package cn.corner.web.browser.conf;

import cn.corner.web.core.authorize.AuthorizeConfigManager;
import cn.corner.web.core.conf.FormLoginConfig;
import cn.corner.web.core.conf.SMSCodeAuthenticationSecurityConfig;
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
 * 浏览器security的总配置，其中应用了核心配置（表单登录、验证码过滤器、手机验证码登录拦截、社交登录）
 * 并且使用AuthorizeConfigManager接口查找用户模块的自定义配置
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
    private FormLoginConfig formLoginConfig;

    /**
     *  注册Social登录需要的过滤器
     */
    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单登录配置
        formLoginConfig.applyLoginConfigure(http);
        http
            // 验证码配置
            .apply(validateCodeFilterConfig)
            .and()
            // 手机验证码登录配置
            .apply(smsCodeAuthenticationSecurityConfig)
            .and()
            // 社交登录配置
            .apply(springSocialConfigurer)
            .and()
                // 记住我功能配置
                .rememberMe()
                    //配置记住我功能token存储
                    .tokenRepository(persistentTokenRepository())
                    //记住我功能有效时长
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    //如果从仓库中找到了有效的token信息，说明记住我功能验证成功，需要调用UserDetailsService
                    //去查找业务的用户信息以便构建Authentication（这样才算security的认证通过）
                    .userDetailsService(userDetailsService)
                    .and()
                // 配置session
                .sessionManagement()
                    .invalidSessionStrategy(invalidSessionStrategy)
                    .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                    .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                    .expiredSessionStrategy(sessionInformationExpiredStrategy)
                    .and()
                    .and()
                .csrf().disable();
        // 授权配置管理类负责将所有的授权配置（core、browser、app、自定义的）进行应用
        authorizeConfigManager.config(http.authorizeRequests());
    }

    /**
     * remember me的token存储仓库配置，这里使用数据库存储（Redis存储没有默认实现）
     * 会在给定的数据源中创建表进行token的存储
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();

        repository.setDataSource(dataSource);
        // 注意：这个方法会在每次启动时创建表，所以第一次之后就会报冲突
        // repository.setCreateTableOnStartup(true);
        return repository;
    }

}
