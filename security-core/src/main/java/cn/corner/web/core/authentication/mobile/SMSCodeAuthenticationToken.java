package cn.corner.web.core.authentication.mobile;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SMSCodeAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 500L;
    private final String mobile;

    public SMSCodeAuthenticationToken(String mobile) {
        super((Collection)null);
        this.mobile = mobile;
        this.setAuthenticated(false);
    }

    public SMSCodeAuthenticationToken(String mobile, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.mobile = mobile;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return this.mobile;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
