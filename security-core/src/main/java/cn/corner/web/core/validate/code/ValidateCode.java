package cn.corner.web.core.validate.code;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class ValidateCode {

    protected String code;

    protected LocalDateTime expireTime;

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }

}
