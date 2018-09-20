package cn.corner.web.core.properties;

import lombok.Data;

@Data
public class ValicateCodeProperties {

    private ImageCodeProperties imageCode = new ImageCodeProperties();

    private SMSCodeProperties smsCode = new SMSCodeProperties();
}
