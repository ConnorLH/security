package cn.corner.web.core.logout;

import cn.corner.web.core.support.SimpleSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private String signOutUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    public MyLogoutSuccessHandler(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("退出操作成功！");
        // 这里就可以做退出后的处理操作

        if (StringUtils.isBlank(signOutUrl)) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new SimpleSupport("退出登录成功")));
        } else {
            httpServletResponse.sendRedirect(signOutUrl);
        }
    }
}
