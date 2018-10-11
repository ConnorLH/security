package cn.corner.web.core.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 社交账号绑定状态视图
 * 社交账号绑定状态的Endpoint已经在ConnectController中默认提供了
 * 处理社交绑定的Controller是{@link ConnectController}
 */
@Component("connect/status")
public class MyConnectionStatusView extends AbstractView {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>)model.get("connectionMap");
        Map<String, Boolean> result = new HashMap<>();
        for (String key : connections.keySet()){
            result.put(key, CollectionUtils.isNotEmpty(connections.get(key)));
        }

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
