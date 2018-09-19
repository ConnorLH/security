package cn.corner.web.core.validate.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ImageCode {

    public ImageCode(BufferedImage image,String code,int expireTime){
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }

    private BufferedImage image;

    private String code;

    private LocalDateTime expireTime;
}
