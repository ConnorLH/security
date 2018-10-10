package cn.corner.web.core.validate.code.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 验证码封装类
 */
@Data
public abstract class ValidateCode implements Serializable {
    private static final long serialVersionUID = 2582470088978715703L;
    protected String code;

    protected LocalDateTime expireTime;

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }

}
