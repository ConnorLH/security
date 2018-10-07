package cn.corner.web.app.social;

import cn.corner.web.core.social.MySpringSocialConfigurer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * SpringSocialConfigurer这个Bean初始化之后设置app的不同处理方式(即跳到app指定的url上)
 * （太不优雅，需要修改为更加合适的app自定义配置）
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(StringUtils.equals(beanName,"MySpringSocialConfigurer")){
            MySpringSocialConfigurer socialConfigurer = (MySpringSocialConfigurer) bean;
            socialConfigurer.signupUrl("/social/signUp");
            return socialConfigurer;
        }
        return bean;
    }
}
