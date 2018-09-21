package cn.corner.web.core.validate.code.sms;

public class DefaultSMSSender implements SMSCodeSender {
    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机:"+mobile+",发送验证码:"+code);
    }
}
