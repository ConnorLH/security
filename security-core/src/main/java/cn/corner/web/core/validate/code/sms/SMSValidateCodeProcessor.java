package cn.corner.web.core.validate.code.sms;

import cn.corner.web.core.validate.code.AbstractValidateCodeProcessor;
import cn.corner.web.core.validate.code.dto.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

@Component("smsValidateCodeProcessor")
public class SMSValidateCodeProcessor extends AbstractValidateCodeProcessor {

    public static final String SESSION_KEY_SMS = "SESSION_KEY_SMS_CODE";


    @Autowired
    private SMSCodeSender sender;

    @Override
    public void send(ValidateCode validateCode, ServletWebRequest request) throws ServletRequestBindingException {
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile");
        sender.send(mobile,validateCode.getCode());
    }

    @Override
    public void save(ValidateCode validateCode, ServletWebRequest request) {
        getStrategy().setAttribute(request, SESSION_KEY_SMS, validateCode);
    }
}
