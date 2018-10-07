package cn.corner.web.core.validate.code.image;

import cn.corner.web.core.conf.SecurityConstant;
import cn.corner.web.core.validate.code.AbstractValidateCodeConfirmHolder;
import cn.corner.web.core.validate.code.ValidateCodeFailuerException;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

@Component("imageValidateCodeConfirmHolder")
@Slf4j
public class ImageValidateCodeConfirmHolder extends AbstractValidateCodeConfirmHolder {

    @Override
    public void confirm(HttpServletRequest request) {
        String code = "";
        try {
            code = ServletRequestUtils.getStringParameter(request, SecurityConstant.DEFAULT_PARAMETER_NAME_CODE_IMAGE);
        } catch (ServletRequestBindingException e) {
            log.error("无效的验证码参数");
            throw new ValidateCodeFailuerException("无效的验证码参数");
        }
        defaultValidateCode(request,code, ValidateCodeType.IMAGE);
    }
}
