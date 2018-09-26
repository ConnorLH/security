package cn.corner.web.core.social.weixin.api;

import cn.corner.web.core.social.qq.api.QQAPI;
import cn.corner.web.core.social.qq.api.QQUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class WeixinAPIImpl extends AbstractOAuth2ApiBinding implements WeixinAPI {

    private static final String URL_GET_USERINFO="https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeixinAPIImpl(String accessToken){
        super(accessToken,TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而返回的是UTF-8，所以覆盖原来的方法重新注册
     * @return
     */
    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }

    @Override
    public WeixinUserInfo getUserInfo(String openid) {
        String result = getRestTemplate().getForObject(URL_GET_USERINFO+openid, String.class);
        log.info(result);
        if(StringUtils.contains(result,"errcode")){
            return null;
        }
        try {
            return objectMapper.readValue(result, WeixinUserInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("获取用户信息失败");
        }
    }
}
