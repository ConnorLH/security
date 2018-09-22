package cn.corner.web.core.properties;

import lombok.Data;

/**
 * 各类的验证码配置统一写在这里
 * 其中类名必须为XXCodeProperties,其中XX还必须与该验证码ValidateCodeType名相同
 */
@Data
public class ValidateCodeProperties {

    private ImageCodeProperties imageCode = new ImageCodeProperties();

    private SMSCodeProperties smsCode = new SMSCodeProperties();
}
