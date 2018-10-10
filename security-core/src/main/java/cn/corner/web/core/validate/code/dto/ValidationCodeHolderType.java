package cn.corner.web.core.validate.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 验证码类型对应拦截的url封装类
 */
@Data
@AllArgsConstructor
public class ValidationCodeHolderType {

    private ValidateCodeType validateCodeType;

    private List<String> blockUrls;
}
