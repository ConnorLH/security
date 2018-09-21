package cn.corner.web.core.validate.code.image;

import cn.corner.web.core.validate.code.AbstractValidateCodeProcessor;
import cn.corner.web.core.validate.code.dto.ValidateCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.io.IOException;

@Component("imageValidateCodeProcessor")
public class ImageValidateCodeProcessor extends AbstractValidateCodeProcessor {

    public static final String SESSION_KEY_IMAGE = "SESSION_KEY_IMAGE_CODE";


    @Override
    public void send(ValidateCode validateCode, ServletWebRequest request) throws IOException {
        ImageIO.write(((ImageCode) validateCode).getImage(), "JPEG", request.getResponse().getOutputStream());
    }

    @Override
    public void save(ValidateCode validateCode, ServletWebRequest request) {
        getStrategy().setAttribute(request, SESSION_KEY_IMAGE, validateCode);
        request.getResponse().setContentType("image/jpeg");
    }
}
