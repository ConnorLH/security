package cn.corner.web.core.validate.code.sms;

import cn.corner.web.core.properties.SecurityProperties;
import cn.corner.web.core.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 短信验证码生成器
 */
public class SMSCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 可以根据请求参数生成不同随机数范围的验证码（具有可配置的默认值）
     * 参数
     * length
     * @param request
     * @return
     */
    @Override
    public SMSCode buildCode(HttpServletRequest request) {
        String randomNumeric = RandomStringUtils.randomNumeric(ServletRequestUtils.getIntParameter(request,"length",securityProperties.getCode().getSmsCode().getLength()));
        return new SMSCode(randomNumeric,ServletRequestUtils.getIntParameter(request,"expire",securityProperties.getCode().getSmsCode().getExpire()));
    }
}
