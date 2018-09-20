package cn.corner.web.core.validate.code;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConf {

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        return new ImageCodeGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsCodeSender")
    public SMSCodeSender smsCodeSender(){
        return new DefaultSMSSender();
    }
}
