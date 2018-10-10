package cn.corner.web.core.authentication.mobile;

import cn.corner.web.core.conf.SecurityConstant;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 短信登录过滤器，主要处理用户信息匹配
 * 不再校验验证码，因为在之前的ValidateCodeFilter中已经进行了校验
 */
public class SMSCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private String mobileParameter = SecurityConstant.CORNER_FORM_MOBILE_KEY;

    private boolean postOnly = true;

    public SMSCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstant.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String mobile = this.obtainMobile(request);
            if (mobile == null) {
                mobile = "";
            }

            mobile = mobile.trim();
            // 将请求参数封装成AuthenticationToken交给Provider进行认证
            SMSCodeAuthenticationToken authRequest = new SMSCodeAuthenticationToken(mobile);
            this.setDetails(request, authRequest);
            // AuthenticationManager查找（调supports方法）相应的Provider进行认证
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(this.mobileParameter);
    }

    /**
     * 将请求中的用户信息也设置到认证成功的Authentication中
     * @param request
     * @param authRequest
     */
    protected void setDetails(HttpServletRequest request, SMSCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "Username parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return this.mobileParameter;
    }

}
