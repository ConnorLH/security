package cn.corner.web.app;

/**
 * app密码错误异常
 */
public class AppSecretException extends RuntimeException {

    public AppSecretException(String msg){
        super(msg);
    }
}
