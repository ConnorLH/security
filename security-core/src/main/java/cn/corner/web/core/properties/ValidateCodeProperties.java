package cn.corner.web.core.properties;

import lombok.Data;

@Data
public class ValidateCodeProperties {

    private ImageCodeProperties imageCode = new ImageCodeProperties();

    private SMSCodeProperties smsCode = new SMSCodeProperties();
}
