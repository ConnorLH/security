package cn.corner.web.core.validate.code.sms;

import cn.corner.web.core.properties.SecurityProperties;
import cn.corner.web.core.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class SMSCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public SMSCode buildCode(HttpServletRequest request) {
        String randomNumeric = RandomStringUtils.randomNumeric(ServletRequestUtils.getIntParameter(request,"length",securityProperties.getCode().getSmsCode().getLength()));
        return new SMSCode(randomNumeric,ServletRequestUtils.getIntParameter(request,"expire",securityProperties.getCode().getSmsCode().getExpire()));
    }
}
