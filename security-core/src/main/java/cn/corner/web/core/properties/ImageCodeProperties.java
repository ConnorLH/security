package cn.corner.web.core.properties;

import lombok.Data;

@Data
public class ImageCodeProperties {

    private int width = 64;

    private int height = 40;

    private int length = 4;

    private int expire = 60;

    private String url;

}
