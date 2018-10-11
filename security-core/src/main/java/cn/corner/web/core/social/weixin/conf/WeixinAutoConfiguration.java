/**
 *
 */
package cn.corner.web.core.social.weixin.conf;

import cn.corner.web.core.properties.SecurityProperties;
import cn.corner.web.core.properties.social.WeixinProperties;
import cn.corner.web.core.social.MyConnectionView;
import cn.corner.web.core.social.weixin.connect.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.web.servlet.View;

/**
 * 微信登录配置
 *
 * @author zhailiang
 */
@Configuration
@ConditionalOnProperty(prefix = "corner.security.social.weixin", name = "app-id")
public class WeixinAutoConfiguration implements SocialConfigurer {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(createConnectionFactory());
    }

    @Override
    public UserIdSource getUserIdSource() {
        return null;
    }

    public ConnectionFactory<?> createConnectionFactory() {
        WeixinProperties weixinConfig = securityProperties.getSocial().getWeixin();
        return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
                weixinConfig.getAppSecret());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

    /**
     * 配置默认绑订成功的Controller
     * @return
     */
    @Bean({"connect/weixinConnected","connect/weixinConnect"})
    @ConditionalOnMissingBean(name = "weixinConnectedView")
    public View weixinConnectedView(){
        return new MyConnectionView();
    }
}
