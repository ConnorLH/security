package cn.corner.web.core.conf;

/**
 * security相关常量配置
 */
public interface SecurityConstant {

    /**
     * 请求登录页的url
     */
    public static final String LOGIN_PAGE_URL="/authentication/require";

    /**
     * 拦截的登录请求url
     */
    public static final String LOGIN_PROCESS_URL="/authentication/form";

    /**
     * 验证码请求url
     */
    public static final String VALIDATE_CODE_URL="/code/*";

    public  static final String SOCIAL_LOGIN_PAGE="/login/*";

    public static final String DEFAULT_SESSION_INVALID_URL = "/my-invalid.html";
}
