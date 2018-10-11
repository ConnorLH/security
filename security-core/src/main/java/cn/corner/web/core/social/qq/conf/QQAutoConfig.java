package cn.corner.web.core.social.qq.conf;

import cn.corner.web.core.properties.SecurityProperties;
import cn.corner.web.core.properties.social.QQProperties;
import cn.corner.web.core.social.MyConnectionView;
import cn.corner.web.core.social.qq.connect.QQConnectionFactory;
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
 * 将我们的QQConnectionFactory设置到Security Social中
 */
@Configuration
@ConditionalOnProperty(prefix = "corner.security.social.qq",name = "app-id")
public class QQAutoConfig implements SocialConfigurer {

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

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

    public ConnectionFactory<?> createConnectionFactory() {
        QQProperties qq = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret());
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
