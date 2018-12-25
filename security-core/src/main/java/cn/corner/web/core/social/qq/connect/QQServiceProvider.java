package cn.corner.web.core.social.qq.connect;

import cn.corner.web.core.social.qq.api.QQAPI;
import cn.corner.web.core.social.qq.api.QQAPIImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * QQ服务提供者
 * 提供oauth2 api连接的实现即QQOauth2Template
 * 提供oauth2授权码流程操作类OAuth2Operations（已经默认提供了一个实现）
 * 提供业务数据api的实现
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQAPI> {

    private String appId;

    private static final String URL_AUTHORIZE="https://graph.qq.com/oauth2.0/authorize";

    private static final String ACCESSTOKEN="https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId,String appSecret) {
        super(new QQOauth2Template(appId,appSecret,URL_AUTHORIZE,ACCESSTOKEN));
        this.appId = appId;
    }

    /**
     * 每个用户都有不同的AbstractOAuth2ApiBinding，因为我们把openId封装在QQAPI的实现中了
     * @param accessToken
     * @return
     */
    @Override
    public QQAPI getApi(String accessToken) {
        // 能否在这里就用accessToken去换openId,然后将accessToken和openId设置到ThreadLocal中，这样，QQAPIImpl只需要创建一次就行了？？？？？
        return new QQAPIImpl(accessToken,appId);
    }
}
