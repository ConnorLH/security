package cn.corner.web.core.validate.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy strategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals(httpServletRequest.getRequestURI(),"/authentication/form")
                && StringUtils.equals(httpServletRequest.getMethod(), HttpMethod.POST.name())){
            try{
                validateCode(httpServletRequest);
            }catch (ValidateCodeFailuerException e){
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }


    private void validateCode(HttpServletRequest request) throws ServletRequestBindingException {
        ImageCode imageCode = (ImageCode)strategy.getAttribute(new ServletWebRequest(request), ValidateCodeController.SESSION_KEY);
        String code = ServletRequestUtils.getStringParameter(request,"code");
        if(StringUtils.isBlank(code)){
            throw new ValidateCodeFailuerException("验证码不能为空");
        }
        if(imageCode == null){
            throw new ValidateCodeFailuerException("验证码已过期");
        }
        if(imageCode.isExpired()){
            strategy.removeAttribute(new ServletWebRequest(request), ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeFailuerException("验证码已过期");
        }
        if(!StringUtils.equals(imageCode.getCode(),code)){
            throw new ValidateCodeFailuerException("验证码不匹配");
        }
        // 通过清空验证码
        strategy.removeAttribute(new ServletWebRequest(request), ValidateCodeController.SESSION_KEY);
    }
}
