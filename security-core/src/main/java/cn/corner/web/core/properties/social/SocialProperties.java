package cn.corner.web.core.properties.social;

import lombok.Data;

@Data
public class SocialProperties {

    private String filterProcessesUrl = "/auth";

    private QQProperties qq = new QQProperties();
}
