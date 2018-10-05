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
     * 默认的手机验证码登录请求处理url
     */
    public static final String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/authentication/mobile";

    /**
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    public static final String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";

    /**
     * 发送短信验证码 或 验证短信验证码时，前端传递手机号的参数的名称
     */
    public static final String CORNER_FORM_MOBILE_KEY = "mobile";

    /**
     * 验证码请求url
     */
    public static final String VALIDATE_CODE_URL="/code/*";

    public  static final String SOCIAL_LOGIN_PAGE="/login/*";

    public static final String DEFAULT_SESSION_INVALID_URL = "/my-invalid.html";
}
