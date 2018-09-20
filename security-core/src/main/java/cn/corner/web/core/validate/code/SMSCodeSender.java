package cn.corner.web.core.validate.code;

public interface SMSCodeSender {

    void send(String mobile,String code);
}
