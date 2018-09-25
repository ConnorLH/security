package cn.corner.web.core.properties;

import cn.corner.web.core.properties.social.QQProperties;
import lombok.Data;

@Data
public class SocialProperties {
    private QQProperties qq = new QQProperties();
}
