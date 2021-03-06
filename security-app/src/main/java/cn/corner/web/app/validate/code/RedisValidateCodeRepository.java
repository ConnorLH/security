package cn.corner.web.app.validate.code;

import cn.corner.web.core.validate.code.ValidateCodeFailuerException;
import cn.corner.web.core.validate.code.ValidateCodeRepository;
import cn.corner.web.core.validate.code.dto.ValidateCode;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * 将验证码存储在redis中
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
        redisTemplate.opsForValue().set(buildKey(request,validateCodeType),code,30, TimeUnit.MINUTES);
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        Object value = redisTemplate.opsForValue().get(buildKey(request, validateCodeType));
        return value == null ? null : (ValidateCode)value;
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        redisTemplate.delete(buildKey(request, validateCodeType));
    }

    /**
     * 生成redis验证码存储key
     * 必须要求请求头中有deviceId以此作为查找主键（标识每一个设备）
     * @param request
     * @param validateCodeType
     * @return
     */
    private String buildKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
        String deviceId = request.getHeader("deviceId");
        if(StringUtils.isBlank(deviceId)){
            throw new ValidateCodeFailuerException("请求头中无deviceId参数");
        }
        return "code:"+validateCodeType.toString().toLowerCase()+":"+deviceId;
    }
}
