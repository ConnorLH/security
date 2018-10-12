package cn.corner.web.app.social;

import cn.corner.web.core.conf.SecurityConstant;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个过滤器实现app模式下用户使用OpenId的方式（其实就是OAuth2的简化模式）进行社交登录，主要逻辑就是使用openId作为登录用户名（没有密码等），数据库有对应openId的数据就行
 */
public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String openIdParameter = SecurityConstant.DEFAULT_PARAMETER_NAME_OPENID;

    private String providerIdParameter = SecurityConstant.DEFAULT_PARAMETER_NAME_PROVIDERID;

    private boolean postOnly = true;

    public OpenIdAuthenticationFilter(){
        super(new AntPathRequestMatcher(SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_OPENID, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String openId = obtainOpenId(request);
        String providerId = obtainProviderId(request);
        if(openId == null){
            openId = "";
        }
        if(providerId == null){
            providerId = "";
        }
        openId = openId.trim();
        providerId = providerId.trim();

        OpenIdAuthenticationToken authRequest = new OpenIdAuthenticationToken(openId,providerId);
        this.setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private String obtainProviderId(HttpServletRequest request) {
        return new ServletWebRequest(request).getParameter(providerIdParameter);
    }

    private String obtainOpenId(HttpServletRequest request) {
       return new ServletWebRequest(request).getParameter(openIdParameter);
    }

    protected void setDetails(HttpServletRequest request, OpenIdAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
