package cn.corner.web.core.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 表单登录、登出、密码处理核心配置
 * 指明了需要进行认证拦截的配置等
 * PS：其中请求（跳转）登录页和登录请求处理的url这里是core包配置死了的
 * 后续可作为重构点
 */
@Configuration
@ConditionalOnMissingBean(name="formLoginConfig")
public class DefaultFormLoginConfig implements FormLoginConfig {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    /**
     * 主配置（即继承了WebSecurityConfigurerAdapter的类）处进行选择性调用，
     * 即其他模块可以自定义这个配置类
     * @param http
     * @throws Exception
     */
    public void applyLoginConfigure(HttpSecurity http) throws Exception {
        http
            // 表单登录配置
            .formLogin()
                // 需要登录时让其重定向到这个url
                .loginPage(SecurityConstant.LOGIN_PAGE_URL)
                // 登录处理url
                .loginProcessingUrl(SecurityConstant.LOGIN_PROCESS_URL)
                // 登录成功handler
                .successHandler(authenticationSuccessHandler)
                // 登录失败handler
                .failureHandler(authenticationFailureHandler)
                .and()
            // 登出配置
            .logout()
                // 登出处理url
                .logoutUrl("/signOut")
                // success和url是互斥的，只能配一个
                // 登出成功后重定向的url
                //.logoutSuccessUrl("/mylogout.html")
                // 登出成功处理handler
                .logoutSuccessHandler(logoutSuccessHandler)
                // 配置登出后删除浏览器cookie
                .deleteCookies("JSESSIONID");
    }

    /**
     * 构建一个security的密码处理bean，security会自动使用它（不需要额外配置或注册）
     * 这里配置一个security推荐的DelegatingPasswordEncoder，他会根据密码前的标识符（{XXX}）
     * 来查找合适的密码处理器进行参数密码加密
     * 其中默认配置了一堆密码处理器，如果有需要可以仿照代码自己构建DelegatingPasswordEncoder
     * @return
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
