package cn.corner.web.core.conf;

import cn.corner.web.core.validate.code.ValidateCodeGenerator;
import cn.corner.web.core.validate.code.image.ImageCodeGenerator;
import cn.corner.web.core.validate.code.sms.DefaultSMSSender;
import cn.corner.web.core.validate.code.sms.SMSCodeGenerator;
import cn.corner.web.core.validate.code.sms.SMSCodeSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码相关的配置
 */
@Configuration
public class ValidateCodeBeanConf {

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        return new ImageCodeGenerator();
    }

    @Bean("smsCodeGenerator")
    @ConditionalOnMissingBean(name = "smsCodeGenerator")
    public ValidateCodeGenerator sMSCodeGenerator(){
        return new SMSCodeGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsCodeSender")
    public SMSCodeSender smsCodeSender(){
        return new DefaultSMSSender();
    }
}
