package cn.corner.web.core.properties;

import lombok.Data;

@Data
public class SMSCodeProperties {

    private int length = 4;

    private int expire = 60;

    private String url;

}
