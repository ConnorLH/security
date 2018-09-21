package cn.corner.web.core.validate.code;

import javax.servlet.http.HttpServletRequest;

public interface ValidateCodeConfirmHolder {
    void confirm(HttpServletRequest request);
}
