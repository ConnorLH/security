package cn.corner.web.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeProcessor {

    void validateCodeCreate(ServletWebRequest request);
}
