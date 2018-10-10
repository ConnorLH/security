package cn.corner.web.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 验证码生成入口controller
 */
@RestController
public class ValidateCodeController {

    /**
     * 依赖查找方式注入
     */
    @Autowired
    private Map<String,ValidateCodeProcessor> validateCodeProcessor;

    /**
     * 根据type进行配置
     * 其中参数必须满足ValidateCodeType名小写
     * @param request
     * @param response
     * @param type
     */
    @GetMapping("/code/{type}")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) {
        validateCodeProcessor.get(type+"ValidateCodeProcessor").validateCodeCreate(new ServletWebRequest(request,response));
    }
}
