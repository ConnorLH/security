package cn.corner.web.core.validate.code;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证码验证处理器
 */
public interface ValidateCodeConfirmHolder {
    void confirm(HttpServletRequest request);
}
