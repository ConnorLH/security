package cn.corner.web.core.validate.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractValidateCodeProcessor<T extends ValidateCode> implements ValidateCodeProcessor {

    @Autowired
    private Map<String, ValidateCodeGenerator> generator;

    @Override
    public void validateCodeCreate(ServletWebRequest request) {
        try {
            T validateCode = generateCodeByURL(request);
            save(validateCode, request);
            send(validateCode, request);
        } catch (Exception e) {
            throw new ValidateCodeFailuerException("创建验证码出错");
        }
    }

    protected abstract void send(T validateCode, ServletWebRequest request) throws ServletRequestBindingException, IOException;

    protected abstract void save(T validateCode, ServletWebRequest request);

    /**
     * 默认根据uri生成对应的验证码，可以覆盖实现
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    protected T generateCodeByURL(ServletWebRequest request) {
        String requestURI = request.getRequest().getRequestURI();
        if (requestURI.startsWith("/code")) {
            String typeString = StringUtils.substringAfter(requestURI, "/code/");
            if (StringUtils.equalsIgnoreCase(ValidateCodeType.IMAGE.getType(), typeString)) {
                return (T) generator.get("imageCodeGenerator").buildCode(request.getRequest());
            } else if (StringUtils.equalsIgnoreCase(ValidateCodeType.SMS.getType(), typeString)) {
                return (T) generator.get("sMSCodeGenerator").buildCode(request.getRequest());
            }
        }
        throw new ValidateCodeFailuerException("请求验证码路径错误");
    }
}
