package cn.corner.web.core.validate.code;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ImageCode extends ValidateCode {

    public ImageCode(BufferedImage image,String code,int expireTime){
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    private BufferedImage image;

}
