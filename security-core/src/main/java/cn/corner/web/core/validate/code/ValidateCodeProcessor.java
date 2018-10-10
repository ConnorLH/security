package cn.corner.web.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成处理器接口，用于创建、存储、发送验证码（模板方法）
 */
public interface ValidateCodeProcessor {

    void validateCodeCreate(ServletWebRequest request);
}
