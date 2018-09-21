package cn.corner.web.core.validate.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationCodeHolderType {

    private ValidateCodeType validateCodeType;

    private List<String> blockUrls;
}
