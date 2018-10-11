package cn.corner.web.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 *自定义SpringSocialConfigurer配置
 *主要用于设置SocialAuthenticationFilter里面的一些配置
 *主要作用是与个性化模块的配置做一个连接，让他们有机会在SocialAuthenticationFilter上设置自己个性化的东西
 */
public class MySpringSocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    private SocialAuthenticationFilterPostProcessor postProcessor;

    public MySpringSocialConfigurer(String filterProcessesUrl){
        this.filterProcessesUrl = filterProcessesUrl;
    }

    /**
     * 配置社交登陆拦截的url
     * 设置社交登录后的处理器（注意与表单登录处设置登录成功处理器区别，这是两种登录方式）
     * @param object
     * @param <T>
     * @return
     */
    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter)super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);

        if(postProcessor!=null){
            postProcessor.process(filter);
        }
        return (T)filter;
    }

    public SocialAuthenticationFilterPostProcessor getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(SocialAuthenticationFilterPostProcessor postProcessor) {
        this.postProcessor = postProcessor;
    }
}
