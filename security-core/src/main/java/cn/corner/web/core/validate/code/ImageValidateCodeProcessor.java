package cn.corner.web.core.validate.code;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.io.IOException;

@Component("imageValidateCodeProcessor")
public class ImageValidateCodeProcessor extends AbstractValidateCodeProcessor {

    public static final String SESSION_KEY_IMAGE = "SESSION_KEY_IMAGE_CODE";

    private SessionStrategy strategy = new HttpSessionSessionStrategy();


    @Override
    protected void send(ValidateCode validateCode, ServletWebRequest request) throws IOException {
        ImageIO.write(((ImageCode) validateCode).getImage(), "JPEG", request.getResponse().getOutputStream());
    }

    @Override
    protected void save(ValidateCode validateCode, ServletWebRequest request) {
        strategy.setAttribute(request, SESSION_KEY_IMAGE, validateCode);
        request.getResponse().setContentType("image/jpeg");
    }
}
