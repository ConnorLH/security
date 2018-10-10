package cn.corner.web.core.conf;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 表单登录配置类接口，可用户模块以进行自定义覆盖
 */
public interface FormLoginConfig {
    void applyLoginConfigure(HttpSecurity http) throws Exception;
}
