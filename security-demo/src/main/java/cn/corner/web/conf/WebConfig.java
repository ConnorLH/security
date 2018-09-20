package cn.corner.web.conf;

import cn.corner.web.interceptor.MyCallableInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MyCallableInterceptor interceptor;
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.registerCallableInterceptors(interceptor);
        //configurer.registerDeferredResultInterceptors();
    }

    /*@Bean
    public ValidateCodeGenerator imageCodeGenerator(){
        return new DemoImageCodeGenerator();
    }*/
}
