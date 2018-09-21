package cn.corner.web.core.validate.code.image;

import cn.corner.web.core.validate.code.dto.ValidateCode;
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
