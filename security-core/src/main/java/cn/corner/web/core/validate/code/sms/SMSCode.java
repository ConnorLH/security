package cn.corner.web.core.validate.code.sms;

import cn.corner.web.core.validate.code.dto.ValidateCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class SMSCode extends ValidateCode {

    public SMSCode(){}

    public SMSCode(String code, int expireTime){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

}
