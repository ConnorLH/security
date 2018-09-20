package cn.corner.web.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

@Component("smsValidateCodeProcessor")
public class SMSValidateCodeProcessor extends AbstractValidateCodeProcessor {

    public static final String SESSION_KEY_SMS = "SESSION_KEY_SMS_CODE";

    private SessionStrategy strategy = new HttpSessionSessionStrategy();

    @Autowired
    private SMSCodeSender sender;

    @Override
    protected void send(ValidateCode validateCode, ServletWebRequest request) throws ServletRequestBindingException {
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile");
        sender.send(mobile,validateCode.getCode());
    }

    @Override
    protected void save(ValidateCode validateCode, ServletWebRequest request) {
        strategy.setAttribute(request, SESSION_KEY_SMS, validateCode);
    }
}
