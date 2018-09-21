package cn.corner.web.conf;

import cn.corner.web.browser.MyUserDetailsService;
import cn.corner.web.core.conf.SMSCodeAuthenticationSecurityConfig;
import cn.corner.web.core.conf.LoginConfig;
import cn.corner.web.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.Filter;
import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;


    @Autowired
    @Qualifier("validateCodeFilter")
    private Filter validateCodeFilter;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SMSCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private LoginConfig loginConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(validateCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    .userDetailsService(userDetailsService)
                    .and()
                .authorizeRequests()
                    .antMatchers("/error","/code/*","/authentication/require",securityProperties.getBrowser().getLoginPage()).permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .apply(smsCodeAuthenticationSecurityConfig)
                .and().apply(loginConfig);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
       // repository.setCreateTableOnStartup(true);
        return repository;
    }

}
