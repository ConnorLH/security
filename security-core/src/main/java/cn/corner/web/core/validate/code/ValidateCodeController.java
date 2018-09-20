package cn.corner.web.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ValidateCodeController {

    @Autowired
    private ValidateCodeProcessor imageValidateCodeProcessor;

    @Autowired
    private ValidateCodeProcessor smsValidateCodeProcessor;

    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) {
        imageValidateCodeProcessor.validateCodeCreate(new ServletWebRequest(request,response));
    }

    @GetMapping("/code/sms")
    public void createSMSCode(HttpServletRequest request, HttpServletResponse response) {
        smsValidateCodeProcessor.validateCodeCreate(new ServletWebRequest(request,response));
    }
}
