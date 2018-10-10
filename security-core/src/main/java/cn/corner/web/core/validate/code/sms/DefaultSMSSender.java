package cn.corner.web.core.validate.code.sms;

/**
 * 默认的短信发送器
 * 这里只是简单打印日志
 * 用户模块需要覆盖实现
 */
public class DefaultSMSSender implements SMSCodeSender {
    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机:"+mobile+",发送验证码:"+code);
    }
}
