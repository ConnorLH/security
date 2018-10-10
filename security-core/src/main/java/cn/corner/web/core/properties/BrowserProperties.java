package cn.corner.web.core.properties;

import lombok.Data;

@Data
public class BrowserProperties {

    private String signUpUrl = "/signUp.html";

    private String loginPage = "/mysign.html";

    private String signOutUrl = "";

    private String registUrl = "/user/regist";

    private LoginType loginType = LoginType.JSON;

    private int rememberMeSeconds = 3600;

    private SessionProperties session = new SessionProperties();
}
