package cn.corner.web.core.validate.code;

import cn.corner.web.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy strategy = new HttpSessionSessionStrategy();

    private Set<String> urls = new HashSet<>();

    @Autowired
    private SecurityProperties securityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        boolean action =false;

        for (String url : urls) {
            if(antPathMatcher.match(url,httpServletRequest.getRequestURI())){
                action = true;
                break;
            }
        }
        if (action) {
            try {
                validateCode(httpServletRequest);
            } catch (ValidateCodeFailuerException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private void validateCode(HttpServletRequest request) throws ServletRequestBindingException {
        ImageCode imageCode = (ImageCode) strategy.getAttribute(new ServletWebRequest(request), ImageValidateCodeProcessor.SESSION_KEY_IMAGE);
        String code = ServletRequestUtils.getStringParameter(request, "code");
        if (StringUtils.isBlank(code)) {
            throw new ValidateCodeFailuerException("验证码不能为空");
        }
        if (imageCode == null) {
            throw new ValidateCodeFailuerException("验证码已过期");
        }
        if (imageCode.isExpired()) {
            strategy.removeAttribute(new ServletWebRequest(request), ImageValidateCodeProcessor.SESSION_KEY_IMAGE);
            throw new ValidateCodeFailuerException("验证码已过期");
        }
        if (!StringUtils.equals(imageCode.getCode(), code)) {
            throw new ValidateCodeFailuerException("验证码不匹配");
        }
        // 通过清空验证码
        strategy.removeAttribute(new ServletWebRequest(request), ImageValidateCodeProcessor.SESSION_KEY_IMAGE);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String configsUrl = securityProperties.getCode().getImageCode().getUrl();
        if(StringUtils.isNotBlank(configsUrl)){
            String[] configs = StringUtils.split(configsUrl, ',');
            urls.addAll(Arrays.asList(configs));
        }
        // 登录请求限制默认必做验证码校验
        urls.add("/authentication/form");
    }
}
