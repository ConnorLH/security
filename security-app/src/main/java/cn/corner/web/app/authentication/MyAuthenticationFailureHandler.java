package cn.corner.web.app.authentication;

import cn.corner.web.core.properties.LoginType;
import cn.corner.web.core.properties.SecurityProperties;
import cn.corner.web.core.support.SimpleSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器，和Browser模块的一样
 */
@Component
@Slf4j
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("登录失败");
        if(LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new SimpleSupport(e.getMessage())));
        }else{
            super.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
        }

    }
}
