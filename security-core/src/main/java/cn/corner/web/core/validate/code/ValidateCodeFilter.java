package cn.corner.web.core.validate.code;

import cn.corner.web.core.properties.SecurityProperties;
import cn.corner.web.core.properties.ValidateCodeProperties;
import cn.corner.web.core.validate.code.dto.ValidateCodeType;
import cn.corner.web.core.validate.code.dto.ValidationCodeHolderType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private Map<String,ValidateCodeConfirmHolder> confirmHolderMap = new HashMap<>();

    private List<ValidationCodeHolderType> urlValidateHolderList = new ArrayList<>();

    @Autowired
    private SecurityProperties securityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 检测是否需要验证码
        ValidateCodeType validateCodeType = getValidateCodeType(httpServletRequest);
        if(validateCodeType!=null){
            try {
                ValidateCodeConfirmHolder confirmHolder = confirmHolderMap.get(StringUtils.lowerCase(validateCodeType.name()) + "ValidateCodeConfirmHolder");
                confirmHolder.confirm(httpServletRequest);
            }catch (ValidateCodeFailuerException e){
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private ValidateCodeType getValidateCodeType(HttpServletRequest httpServletRequest) {
        String requestUrl = httpServletRequest.getRequestURI();
        for(ValidationCodeHolderType holderType:urlValidateHolderList){
            List<String> blockUrls =holderType.getBlockUrls();
            for (String url : blockUrls) {
                if (antPathMatcher.match(url, requestUrl)) {
                    return holderType.getValidateCodeType();
                }
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        final List<Field> codeList = new ArrayList<>();
        ReflectionUtils.doWithFields(ValidateCodeProperties.class, field -> codeList.add(field));
        for (Field field : codeList) {
            try {
                ReflectionUtils.makeAccessible(field);
                // 获取properties类对象
                Object codeProperties = field.get(securityProperties.getCode());

                // 获取该properties的url
                Method getUrlMethod = ReflectionUtils.findMethod(codeProperties.getClass(), "getUrl");
                String urlConfig = (String) getUrlMethod.invoke(codeProperties);
                List<String> urlList = null;
                if(urlConfig!=null){
                    String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlConfig==null?"":urlConfig, ",");
                    urlList = Arrays.asList(urls);
                }else{
                    urlList = new ArrayList<>();
                }

                // 根据properties类名获取对应的ValidateCodeType
                String typeString = StringUtils.substringBefore(field.getType().getSimpleName(), "CodeProperties");
                ValidateCodeType validateCodeType = ValidateCodeType.valueOf(StringUtils.upperCase(typeString));

                urlValidateHolderList.add(new ValidationCodeHolderType(validateCodeType, urlList));
            } catch (IllegalAccessException e) {
                log.error("属性不可访问", e);
            } catch (InvocationTargetException e) {
                log.error("动态获取url异常", e);
            }
        }
    }
}
