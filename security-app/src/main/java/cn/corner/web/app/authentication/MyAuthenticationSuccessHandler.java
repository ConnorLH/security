package cn.corner.web.app.authentication;

import cn.corner.web.core.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * 登录成功将会被调用，主要用于设置app模块特有的token登录方式
 * PS：token和cookie的区别个人认为其实就是对于app这种服务器是否可见，
 * 如果是返回cookie那么app处理起来很不方便（还需要自己去实现浏览器那套操作cookie的东西），所以直接把关键的令牌信息字符串给它就行了
 * 以后交流拿这个令牌交换信息方便很多（浏览器会自动携带cookie但是app需要自己实现啊）
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    /**
     * 登录成功后校验Client信息，通过则返回token
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");
        // 先从请求头里面取出client的认证信息（不是用户的，用户的是在参数中，并且走到这里说明已经被认证成功了）
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && (header.startsWith("Basic ") || header.startsWith("basic "))) {
            String[] tokens = this.extractAndDecodeHeader(header, httpServletRequest);
            assert tokens.length == 2;
            String clientId = tokens[0];
            String clientSecret = tokens[1];
            // 根据参数clientId查找存储的ClientDetails
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
            if(clientDetails == null){
                throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:"+clientId);
            }else if(!StringUtils.equals(clientDetails.getClientSecret(),clientSecret)){
                throw new UnapprovedClientAuthenticationException("clientSecret对应的配置信息不存在:"+clientSecret);
            }

            TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP,clientId,clientDetails.getScope(), "custom");

            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request,authentication);
            OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(accessToken));
        } else {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }
    }

    /**
     * 解析请求头中的Authorization部分（被Base64加密过），取出clientId
     * @param header
     * @param request
     * @return
     * @throws IOException
     */
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
        byte[] base64Token = header.substring(6).getBytes("UTF-8");

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        }
    }
}
