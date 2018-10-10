package cn.corner.web.core.social.qq.connect;

import cn.corner.web.core.social.qq.api.QQAPI;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * QQ的OAuth2工厂
 * 可以创建出一套QQ的OAuth2授权使用的类
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQAPI> {
    /**
     *
     * @param providerId 标识使用哪个Oauth2提供商（其实就是标识使用哪个OAuth2工厂）
     *                   这个参数会在进行social登录的时候由请求提供，然后找到对应的工厂处理
     * @param appId 在Oauth2提供商上已经申请的appid
     * @param appSecret 在Oauth2提供商上申请后提供的appSecret
     */
    public QQConnectionFactory(String providerId,String appId,String appSecret) {
        super(providerId, new QQServiceProvider(appId,appSecret), new QQAdapter());
    }
}
