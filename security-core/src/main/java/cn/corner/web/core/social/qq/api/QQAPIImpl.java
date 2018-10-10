package cn.corner.web.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * 对QQ提供的API接口进行调用实现
 */
@Slf4j
public class QQAPIImpl extends AbstractOAuth2ApiBinding implements QQAPI {

    private static final String URL_GET_OPENID="https://graph.qq.com/oauth2.0/me?access_token=%s";

    private static final String URL_GET_USERINFO="https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appid;

    private String openid;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 在构造方法中使用父类提供的RestTemplate来调用QQ的接口获取openid
     * 注意这里需要已经获得access_token
     * @param accessToken
     * @param appid
     */
    public QQAPIImpl(String accessToken,String appid){
        // 父类会自动加上access_token等api调用参数
        super(accessToken,TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appid = appid;

        String url = String.format(URL_GET_OPENID,accessToken);

        String result = getRestTemplate().getForObject(url, String.class);

        log.info(result);

        this.openid = StringUtils.substringBetween(result,"\"openid\":\"","\"}");
    }

    /**
     * 获取用户数据，注意需要使用QQ规定的数据格式进行接收即QQUserInfo
     * @return
     */
    @Override
    public QQUserInfo getUserInfo() {

        String url = String.format(URL_GET_USERINFO,appid,openid);
        String result = getRestTemplate().getForObject(url, String.class);
        log.info(result);
        try {
            QQUserInfo qqUserInfo = objectMapper.readValue(result, QQUserInfo.class);
            qqUserInfo.setOpenId(openid);
            return qqUserInfo;
        } catch (IOException e) {
            throw new RuntimeException("获取用户信息失败");
        }
    }
}
