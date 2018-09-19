package cn.corner.web.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "corner.security")
@Data
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

}
