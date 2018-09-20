package cn.corner.web.core.properties;

import lombok.Data;

@Data
public class BrowserProperties {

    private String loginPage = "/mysmssign.html";

    private LoginType loginType = LoginType.JSON;

    private int rememberMeSeconds = 3600;

}
