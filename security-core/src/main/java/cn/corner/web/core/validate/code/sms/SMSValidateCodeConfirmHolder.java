package cn.corner.web.core.validate.code.sms;

import cn.corner.web.core.validate.code.AbstractValidateCodeConfirmHolder;
import cn.corner.web.core.validate.code.ValidateCodeFailuerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

@Component("smsValidateCodeConfirmHolder")
@Slf4j
public class SMSValidateCodeConfirmHolder extends AbstractValidateCodeConfirmHolder {

    @Override
    public void confirm(HttpServletRequest request) {
        String smsCode = "";
        try {
            smsCode = ServletRequestUtils.getStringParameter(request, "smsCode");
        } catch (ServletRequestBindingException e) {
            log.error("无效的验证码参数");
            throw new ValidateCodeFailuerException("无效的验证码参数");
        }
        defaultValidateCode(request,smsCode,SMSValidateCodeProcessor.SESSION_KEY_SMS);
    }
}
