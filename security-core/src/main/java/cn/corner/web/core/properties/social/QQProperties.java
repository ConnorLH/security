package cn.corner.web.core.properties.social;

import lombok.Data;

@Data
public class QQProperties {
    /**
     * Application id.
     */
    private String appId;

    /**
     * Application secret.
     */
    private String appSecret;
    private String providerId = "qq";

}
