package cn.corner.web.core.validate.code;

import cn.corner.web.core.validate.code.dto.ValidateCode;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码存储接口，不通的验证码有不同的存储实现
 */
public interface ValidateCodeRepository {

    void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

    ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

    void remove(ServletWebRequest request, ValidateCodeType validateCodeType);
}
