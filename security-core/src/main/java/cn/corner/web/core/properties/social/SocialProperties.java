package cn.corner.web.core.properties.social;

import lombok.Data;

@Data
public class SocialProperties {

    private String[] filterProcessesUrls = {"/auth"};

    private QQProperties qq = new QQProperties();

    private WeixinProperties weixin = new WeixinProperties();
}
