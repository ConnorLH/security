package cn.corner.web.core.validate.code;

import cn.corner.web.core.validate.code.dto.ValidateCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Map;

@Slf4j
public abstract class AbstractValidateCodeProcessor<T extends ValidateCode> implements ValidateCodeProcessor {

    @Autowired
    private Map<String, ValidateCodeGenerator> generator;

    private SessionStrategy strategy = new HttpSessionSessionStrategy();

    public static final String SESSION_KEY_DEFAULT = "SESSION_KEY_DEFAULT_CODE";

    @Override
    public void validateCodeCreate(ServletWebRequest request) {
        try {
            T validateCode = generateCodeByURL(request);
            save(validateCode, request);
            send(validateCode, request);
        } catch (Exception e) {
            log.error("创建验证码出错", e);
            throw new ValidateCodeFailuerException("创建验证码出错");
        }
    }

    protected abstract void send(T validateCode, ServletWebRequest request) throws ServletRequestBindingException, IOException;

    /**
     * 默认保存到session中，暂时先这么实现
     * 以后重构需要构建repository接口和实现类，这里调用它的保存方法
     *
     * @param validateCode
     * @param request
     */
    protected void save(T validateCode, ServletWebRequest request) {
        getStrategy().setAttribute(request, SESSION_KEY_DEFAULT, validateCode);
    }

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
            String typeString = StringUtils.substringAfter(requestURI, "/code/") + "CodeGenerator";
            if (generator.containsKey(typeString)) {
                return (T) generator.get(typeString).buildCode(request.getRequest());
            } else {
                throw new ValidateCodeFailuerException("找不到相应的验证码生成器");
            }
        }
        throw new ValidateCodeFailuerException("请求验证码路径错误");
    }

    public SessionStrategy getStrategy() {
        return strategy;
    }
}
