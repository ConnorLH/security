package cn.corner.web.core.validate.code.sms;

import cn.corner.web.core.conf.SecurityConstant;
import cn.corner.web.core.validate.code.AbstractValidateCodeConfirmHolder;
import cn.corner.web.core.validate.code.ValidateCodeFailuerException;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 短信校验码处理器
 */
@Component("smsValidateCodeConfirmHolder")
@Slf4j
public class SMSValidateCodeConfirmHolder extends AbstractValidateCodeConfirmHolder {

    @Override
    public void confirm(HttpServletRequest request) {
        String smsCode = "";
        try {
            smsCode = ServletRequestUtils.getStringParameter(request, SecurityConstant.DEFAULT_PARAMETER_NAME_CODE_SMS);
        } catch (ServletRequestBindingException e) {
            log.error("无效的验证码参数");
            throw new ValidateCodeFailuerException("无效的验证码参数");
        }
        defaultValidateCode(request,smsCode, ValidateCodeType.SMS);
    }
}
