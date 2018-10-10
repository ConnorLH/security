package cn.corner.web.core.validate.code;

import cn.corner.web.core.validate.code.dto.ValidateCode;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码生成器接口
 */
public interface ValidateCodeGenerator {

    ValidateCode buildCode(HttpServletRequest request);
}
