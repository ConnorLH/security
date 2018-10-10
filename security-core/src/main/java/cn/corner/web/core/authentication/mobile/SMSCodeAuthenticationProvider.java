package cn.corner.web.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 短信验证码登录provider
 * 这里其实没有校验逻辑，因为验证码提前在ValidateCodeFilter做了校验，到这里只需要根据手机号获取用户信息放入Authentication即可
 * 当然也可以在这里做业务上登录的额外校验工作
 */
public class SMSCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 调用UserDetailsService（一般用户模块自己实现）获取业务的用户数据
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        if(userDetails == null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        SMSCodeAuthenticationToken authenticationResult = new SMSCodeAuthenticationToken((String) authentication.getPrincipal(),userDetails.getAuthorities());
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SMSCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
