package cn.corner.web.core.properties;

import lombok.Data;

@Data
public class BrowserProperties {

    private String loginPage = "/mysign.html";

    private LoginType loginType = LoginType.JSON;

}
