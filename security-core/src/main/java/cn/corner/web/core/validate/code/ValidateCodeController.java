package cn.corner.web.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class ValidateCodeController {

    @Autowired
    private Map<String,ValidateCodeProcessor> validateCodeProcessor;

    @GetMapping("/code/{type}")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) {
        validateCodeProcessor.get(type+"ValidateCodeProcessor").validateCodeCreate(new ServletWebRequest(request,response));
    }
}
