package cn.corner.web.core.properties;

import lombok.Data;

@Data
public class BrowserProperties {

    private String signUpUrl = "/signUp.html";

    private String loginPage = "/mysign.html";

    private LoginType loginType = LoginType.JSON;

    private int rememberMeSeconds = 3600;

}
