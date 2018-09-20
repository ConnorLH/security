package cn.corner.web.core.validate.code;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码生成器
 */
public interface ValidateCodeGenerator {

    ValidateCode buildCode(HttpServletRequest request);
}
