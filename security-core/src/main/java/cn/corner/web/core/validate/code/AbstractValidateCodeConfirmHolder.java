package cn.corner.web.core.validate.code;

import cn.corner.web.core.validate.code.dto.ValidateCode;
import cn.corner.web.core.validate.code.sms.SMSValidateCodeProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractValidateCodeConfirmHolder implements ValidateCodeConfirmHolder {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private SessionStrategy strategy = new HttpSessionSessionStrategy();

    protected void defaultValidateCode(HttpServletRequest request, String code,String param) {
        ValidateCode validateCode = (ValidateCode) strategy.getAttribute(new ServletWebRequest(request),param);
        if (StringUtils.isBlank(code)) {
            throw new ValidateCodeFailuerException("验证码不能为空");
        }
        if (validateCode == null) {
            throw new ValidateCodeFailuerException("验证码已过期");
        }
        if (validateCode.isExpired()) {
            strategy.removeAttribute(new ServletWebRequest(request), SMSValidateCodeProcessor.SESSION_KEY_SMS);
            throw new ValidateCodeFailuerException("验证码已过期");
        }
        if (!StringUtils.equals(validateCode.getCode(), code)) {
            throw new ValidateCodeFailuerException("验证码不匹配");
        }
        // 通过清空验证码
        strategy.removeAttribute(new ServletWebRequest(request), SMSValidateCodeProcessor.SESSION_KEY_SMS);
    }
}
