package cn.corner.web.core.validate.code;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SMSCode extends ValidateCode {

    public SMSCode(String code, int expireTime){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

}
