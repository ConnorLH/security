package cn.corner.web.core.validate.code;

import cn.corner.web.core.validate.code.dto.ValidateCode;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import cn.corner.web.core.validate.code.sms.SMSValidateCodeProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractValidateCodeConfirmHolder implements ValidateCodeConfirmHolder {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    protected void defaultValidateCode(HttpServletRequest request,String code, ValidateCodeType codeType) {
        ValidateCode validateCode = validateCodeRepository.get(new ServletWebRequest(request),codeType);
        if (StringUtils.isBlank(code)) {
            throw new ValidateCodeFailuerException("验证码不能为空");
        }
        if (validateCode == null) {
            throw new ValidateCodeFailuerException("验证码已过期");
        }
        if (validateCode.isExpired()) {
            validateCodeRepository.remove(new ServletWebRequest(request),codeType);
            throw new ValidateCodeFailuerException("验证码已过期");
        }
        if (!StringUtils.equals(validateCode.getCode(), code)) {
            throw new ValidateCodeFailuerException("验证码不匹配");
        }
        // 通过清空验证码
        validateCodeRepository.remove(new ServletWebRequest(request),codeType);
    }
}
