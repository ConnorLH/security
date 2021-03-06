package cn.corner.web.core.validate.code.image;

import cn.corner.web.core.validate.code.dto.ValidateCode;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Data
public class ImageCode extends ValidateCode {

    public ImageCode(){}

    public ImageCode(BufferedImage image,String code,int expireTime){
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    private transient BufferedImage image;

}
