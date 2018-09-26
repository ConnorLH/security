package cn.corner.web.core.social.qq.connect;

import cn.corner.web.core.social.qq.api.QQAPI;
import cn.corner.web.core.social.qq.api.QQAPIImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQAPI> {

    private String appId;

    private static final String URL_AUTHORIZE="https://graph.qq.com/oauth2.0/authorize";

    private static final String ACCESSTOKEN="https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId,String appSecret) {
        super(new QQOauth2Template(appId,appSecret,URL_AUTHORIZE,ACCESSTOKEN));
        this.appId = appId;
    }

    @Override
    public QQAPI getApi(String accessToken) {
        return new QQAPIImpl(accessToken,appId);
    }
}
