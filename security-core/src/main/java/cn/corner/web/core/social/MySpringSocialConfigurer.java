package cn.corner.web.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

public class MySpringSocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    private SocialAuthenticationFilterPostProcessor postProcessor;

    public MySpringSocialConfigurer(String filterProcessesUrl){
        this.filterProcessesUrl = filterProcessesUrl;
    }

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
