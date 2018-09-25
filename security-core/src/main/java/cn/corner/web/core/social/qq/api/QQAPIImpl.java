package cn.corner.web.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

@Slf4j
public class QQAPIImpl extends AbstractOAuth2ApiBinding implements QQAPI {

    private static final String URL_GET_OPENID="https://graph.qq.com/oauth2.0/me?access_token=%s";

    private static final String URL_GET_USERINFO="https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appid;

    private String openid;

    private ObjectMapper objectMapper = new ObjectMapper();

    public QQAPIImpl(String accessToken,String appid){
        super(accessToken,TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appid = appid;

        String url = String.format(URL_GET_OPENID,accessToken);

        String result = getRestTemplate().getForObject(url, String.class);

        log.info(result);

        this.openid = StringUtils.substringBetween(result,"\"openid\"","}");
    }

    @Override
    public QQUserInfo getUserInfo() {

        String url = String.format(URL_GET_USERINFO,appid,openid);
        String result = getRestTemplate().getForObject(url, String.class);
        log.info(result);
        try {
            return objectMapper.readValue(result,QQUserInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("获取用户信息失败");
        }
    }
}
