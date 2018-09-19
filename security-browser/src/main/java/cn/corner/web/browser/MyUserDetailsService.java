package cn.corner.web.browser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    // 应该在用户注册时使用这个配好的bean对其密码进行加密然后存储
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("登录用户名:"+s);
//        return new User(s,"123456",AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        log.info("数据库密码:"+encoder.encode("123456"));
        return new User(s,encoder.encode("123456"),true,true,true,true
                ,AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
