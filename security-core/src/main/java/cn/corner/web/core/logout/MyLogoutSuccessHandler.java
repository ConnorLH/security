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

/**
 * 登出成功后会调用这个处理器
 */
@Slf4j
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private String signOutUrl;

    private ObjectMapper objectMapper = new ObjectMapper();

    public MyLogoutSuccessHandler(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }

    /**
     * 如果配置了登出成功后的跳转url那么redirect，否则返回登出成功json数据
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
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
