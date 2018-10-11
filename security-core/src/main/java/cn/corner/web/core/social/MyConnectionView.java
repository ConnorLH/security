package cn.corner.web.core.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 社交账号绑订成功视图，这个视图用户模块一般会自定义，所以这里采用配置bean的方式
 * （在微信和QQ的AutoConfig中配置）
 * 处理社交绑定的Controller是{@link ConnectController}
 */
public class MyConnectionView extends AbstractView {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        if (model.get("connections") == null) {
            httpServletResponse.getWriter().write("<h3>解绑成功</h3>");
        } else {
            httpServletResponse.getWriter().write("<h3>绑订成功</h3>");
        }
    }
}
