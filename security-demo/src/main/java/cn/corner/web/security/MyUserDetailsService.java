package cn.corner.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyUserDetailsService implements UserDetailsService,SocialUserDetailsService {

    // 应该在用户注册时使用这个配好的bean对其密码进行加密然后存储
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("表单登录用户名:"+s);
        return buildUserDetails(s);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        log.info("社交登录用户名:"+s);
        return buildUserDetails(s);
    }

    private SocialUserDetails buildUserDetails(String s) {
//        return new User(s,"123456",AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        log.info("数据库密码:"+encoder.encode("123456"));
        return new SocialUser(s,encoder.encode("123456"),true,true,true,true
                ,AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER"));
    }
}
