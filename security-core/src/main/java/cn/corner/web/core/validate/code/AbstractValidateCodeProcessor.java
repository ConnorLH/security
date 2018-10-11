package cn.corner.web.core.validate.code;

import cn.corner.web.core.validate.code.dto.ValidateCode;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import cn.corner.web.core.validate.code.image.ImageCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Map;

/**
 * 抽象的验证码生成处理器，定义了创建、存储、发送验证码的模板方法
 * 提供默认的生成和保存逻辑
 * @param <T>
 */
@Slf4j
public abstract class AbstractValidateCodeProcessor<T extends ValidateCode> implements ValidateCodeProcessor {

    @Autowired
    private Map<String, ValidateCodeGenerator> generator;

    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    public static final String SESSION_KEY_DEFAULT = "SESSION_KEY_DEFAULT_CODE";

    @Override
    public void validateCodeCreate(ServletWebRequest request) {
        try {
            T validateCode = generateCodeByURL(request);
            save(validateCode, request);
            send(validateCode, request);
        } catch (Exception e) {
            log.error("创建验证码出错", e);
            throw new ValidateCodeFailuerException("创建验证码出错");
        }
    }

    protected abstract void send(T validateCode, ServletWebRequest request) throws ServletRequestBindingException, IOException;

    /**
     * 保存校验码
     *
     * @param validateCode
     * @param request
     */
    protected void save(T validateCode, ServletWebRequest request) {
        // 如果是图片验证码，这里保存的时候不需要保存图片
        if(validateCode instanceof ImageCode){
            ValidateCode imageCode = new ImageCode();
            imageCode.setCode(validateCode.getCode());
            imageCode.setExpireTime(validateCode.getExpireTime());
            validateCodeRepository.save(request,imageCode,getValidateCodeType(request));
        }else{
            validateCodeRepository.save(request,validateCode,getValidateCodeType(request));
        }
    }

    /**
     * 根据具体的验证码处理器实现类名来获取验证码类型
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        String processor = StringUtils.substringBefore(getClass().getSimpleName(), "ValidateCodeProcessor");
        return ValidateCodeType.valueOf(processor.toUpperCase());
    }

    /**
     * 默认根据uri获取验证码类型实现来生成对应的验证码，可以覆盖实现
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    protected T generateCodeByURL(ServletWebRequest request) {
        String requestURI = request.getRequest().getRequestURI();
        if (requestURI.startsWith("/code")) {
            String typeString = StringUtils.substringAfter(requestURI, "/code/") + "CodeGenerator";
            if (generator.containsKey(typeString)) {
                return (T) generator.get(typeString).buildCode(request.getRequest());
            } else {
                throw new ValidateCodeFailuerException("找不到相应的验证码生成器");
            }
        }
        throw new ValidateCodeFailuerException("请求验证码路径错误");
    }

}
