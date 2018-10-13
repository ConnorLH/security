package cn.corner.web.app.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 主要用于设置AuthorizationServer的EndPoint的权限拦截控制,特别是"/oauth/authorize"
 * 因为这个EndPoint需要登录后才能正常执行（需要获取给予授权的用户，即谁给第三方客户端授权的）
 * 所以必须在这里对其进行登录拦截
 * 注意：这里的配置AuthenticationManager在MyAuthorizationServerConfig中应用进去，
 * 所以主要是用于拦截对认证服务器的访问。而resource中有另一套的拦截机制(其中的AuthenticationManager也是新new
 * 的)，注意区分这两者。
 * 理论上必须要配置这个，因为只有配置了这个WebSecurity才能生效，而WebSecurity生效了才能对认证服务的那几个Endpoints进行拦截
 * 什么？哪几个Endpoints？好吧。
 * "/oauth/authorize"  "/oauth/token"   "/oauth/token_key"   "/oauth/check_token" ...
 * =================对不起，上面全是屁话================
 * 只有部分是正确的，的却需要设置Basic的认证方式，因为这种方式是便于和App沟通的，它只需要把用户的用户名和密码放到请求头Authorization中
 * 就能够进行认证了的同时获取code，否则还需要先认证再发一次请求获取code
 * 重点来了，为什么需要暴露AuthenticationManager在MyAuthorizationServerConfig中应用进去，简单来说原因只有一个，为了支持密码模式
 * 的OAuth2认证方式。具体见AuthorizationServerEndpointsConfigurer###getDefaultTokenGranters()
 */
@Configuration
public class APPSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/oauth/authorize").authenticated()
            .and().httpBasic();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
