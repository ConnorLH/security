package cn.corner.web.core.social.qq.connect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 覆盖父类默认实现适配QQ的授权数据
 */
@Slf4j
    public class QQOauth2Template extends OAuth2Template {

    public QQOauth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 请求返回的accesstoken数据是一个字符串不是json
     * 父类默认实现是将其解析为一个map会报错，这里需要覆盖默认实现，自己解析这个返回的字符串
     * @param accessTokenUrl
     * @param parameters
     * @return
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        log.info("获取accessToken的响应:"+responseStr);
        String[] strings = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");
        String accessToken = StringUtils.substringAfter(strings[0], "=");
        Long expiresIn = Long.valueOf(StringUtils.substringAfter(strings[1], "="));
        String refreshToken = StringUtils.substringAfter(strings[2], "=");
        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }

    /**
     * 给RestTemplate添加了UTF-8编码适配返回的编码
     * @return
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }


}
