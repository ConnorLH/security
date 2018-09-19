package cn.corner.web.core.validate.code;

import org.springframework.security.core.AuthenticationException;

public class ValidateCodeFailuerException extends AuthenticationException {
    public ValidateCodeFailuerException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidateCodeFailuerException(String msg) {
        super(msg);
    }
}
