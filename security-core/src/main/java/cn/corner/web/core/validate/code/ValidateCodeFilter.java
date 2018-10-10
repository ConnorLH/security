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

/**
 * 这个过滤器负责处理各种形式的验证码，图形、短信（注意与短信登录功能的区别，短信验证码只是验证对不对，短信登录还需要做其他的登录操作）
 * 注意：这个过滤器的功能只有两个
 * 1、验证哪些url需要进行验证码验证
 * 2、调用相应的处理器进行校验
 * 验证码的生成逻辑在Controller中实现
 */
@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private Map<String,ValidateCodeConfirmHolder> confirmHolderMap = new HashMap<>();

    private List<ValidationCodeHolderType> urlValidateHolderList = new ArrayList<>();

    @Autowired
    private SecurityProperties securityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 如果请求的url需要验证码验证，则根据该url查找相应的验证码处理器进行校验
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 检测是否需要验证码
        List<ValidateCodeType> validateCodeTypeList = getValidateCodeType(httpServletRequest);
        if(validateCodeTypeList.size()>0){
            try {
                for(ValidateCodeType validateCodeType : validateCodeTypeList){
                    ValidateCodeConfirmHolder confirmHolder = confirmHolderMap.get(StringUtils.lowerCase(validateCodeType.name()) + "ValidateCodeConfirmHolder");
                    confirmHolder.confirm(httpServletRequest);
                }
            }catch (ValidateCodeFailuerException e){
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 根据请求的url查找拦截这个url的验证码类型
     * @param httpServletRequest
     * @return
     */
    private List<ValidateCodeType> getValidateCodeType(HttpServletRequest httpServletRequest) {
        List<ValidateCodeType> validateCodeTypeList = new ArrayList<>();
        String requestUrl = httpServletRequest.getRequestURI();
        for(ValidationCodeHolderType holderType:urlValidateHolderList){
            List<String> blockUrls =holderType.getBlockUrls();
            for (String url : blockUrls) {
                if (antPathMatcher.match(url, requestUrl)) {
                    validateCodeTypeList.add(holderType.getValidateCodeType());
                }
            }
        }
        return validateCodeTypeList;
    }


    /**
     * Bean初始化之后调用，查找配置的所有验证码处理器，并添加到map中
     * 具体实现是从ValidateCodeProperties这个配置类中去取所有的Properties再从每个Properties中
     * 取出各验证码处理器和其需要拦截的url进行包装后存到list中
     * @throws ServletException
     */
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
