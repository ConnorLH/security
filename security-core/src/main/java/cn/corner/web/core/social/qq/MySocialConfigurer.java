package cn.corner.web.core.social.qq;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

public class MySocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    public MySocialConfigurer(String filterProcessesUrl){
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter fliter = (SocialAuthenticationFilter)super.postProcess(object);
        fliter.setFilterProcessesUrl(filterProcessesUrl);
        return (T)fliter;
    }
}
