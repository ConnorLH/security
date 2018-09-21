package cn.corner.web.core.validate.code.sms;

public interface SMSCodeSender {

    void send(String mobile,String code);
}
