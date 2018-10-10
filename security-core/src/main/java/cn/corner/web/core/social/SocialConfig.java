package cn.corner.web.core.social;

import cn.corner.web.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfiguration;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 社交登录总配置类
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private  SocialConfiguration socialConfiguration;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor postProcessor;

    /**
     * 用于存储social信息和业务数据用户id对应关系
     * @param connectionFactoryLocator
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        // 可以指定表前缀
        // repository.setTablePrefix("xxx_");
        // 设置自动注册处理器
        if(connectionSignUp != null){
            repository.setConnectionSignUp(connectionSignUp );
        }
        return repository;
    }

    /**
     * 设置从哪里获取用户id，这里是从Authentication中获取（也有默认实现的session中获取）
     * @return
     */
    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    /**
     * 自定义SpringSocialConfigurer配置
     * 主要作用是与个性化模块的配置做一个连接，让他们有机会在SocialAuthenticationFilter上设置自己个性化的东西
     * @return
     */
    @Bean
    public SpringSocialConfigurer mySocialConfigurer(){
        MySpringSocialConfigurer configurer = new MySpringSocialConfigurer(securityProperties.getSocial().getFilterProcessesUrl());
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        configurer.setPostProcessor(postProcessor);
        return configurer;
    }

    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator){
        return new ProviderSignInUtils(connectionFactoryLocator,getUsersConnectionRepository(connectionFactoryLocator));
    }

    /**
     * 注册Spring-social自带的ConnectController
     * 用于处理授权请求
     * @return
     */
    @Bean
    public ConnectController connectController(
            ConnectionFactoryLocator connectionFactoryLocator,
            ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }
}
