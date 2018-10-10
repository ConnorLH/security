package cn.corner.web.core.validate.code.sms;

/**
 * 短信验证码发送器接口
 */
public interface SMSCodeSender {

    void send(String mobile,String code);
}
