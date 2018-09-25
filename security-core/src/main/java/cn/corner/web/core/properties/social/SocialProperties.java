package cn.corner.web.core.properties.social;

import cn.corner.web.core.properties.social.QQProperties;
import lombok.Data;

@Data
public class SocialProperties {

    private String filterProcessUrl = "/auth";

    private QQProperties qq = new QQProperties();
}
