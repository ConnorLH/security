package cn.corner.web.browser.validate.code;

import cn.corner.web.core.validate.code.ValidateCodeRepository;
import cn.corner.web.core.validate.code.dto.ValidateCode;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 将验证码存储在session中
 */
@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {

    public static final String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    private SessionStrategy strategy = new HttpSessionSessionStrategy();

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
        getStrategy().setAttribute(request, getSessionKey(request, validateCodeType),code);
    }

    private String getSessionKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
        return SESSION_KEY_PREFIX + validateCodeType.toString().toUpperCase();
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        return (ValidateCode) strategy.getAttribute(request,getSessionKey(request, validateCodeType));
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        strategy.removeAttribute(request, getSessionKey(request, validateCodeType));
    }

    public SessionStrategy getStrategy() {
        return strategy;
    }
}
