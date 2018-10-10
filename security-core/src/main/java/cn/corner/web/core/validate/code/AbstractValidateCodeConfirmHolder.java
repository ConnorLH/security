package cn.corner.web.core.validate.code;

import cn.corner.web.core.validate.code.dto.ValidateCode;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 抽象的验证码验证处理器
 * 这里提供一个默认的验证逻辑
 */
public abstract class AbstractValidateCodeConfirmHolder implements ValidateCodeConfirmHolder {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 验证码存储接口
     */
    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    /**
     * 从验证码存储中获取之前请求生成的验证码再进行对比
     * @param request
     * @param code
     * @param codeType
     */
    protected void defaultValidateCode(HttpServletRequest request,String code, ValidateCodeType codeType) {
        ValidateCode validateCode = validateCodeRepository.get(new ServletWebRequest(request),codeType);
        if (StringUtils.isBlank(code)) {
            throw new ValidateCodeFailuerException("验证码不能为空");
        }
        if (validateCode == null) {
            throw new ValidateCodeFailuerException("验证码未生成");
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
