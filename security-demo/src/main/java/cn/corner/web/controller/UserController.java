package cn.corner.web.controller;

import cn.corner.web.app.social.AppSignUpUtils;
import cn.corner.web.core.properties.SecurityProperties;
import cn.corner.web.dto.User;
import com.fasterxml.jackson.annotation.JsonView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private AppSignUpUtils appSignUpUtils;

    @Autowired
    private SecurityProperties securityProperties;

    @PostMapping("/regist")
    public void regist(User user, HttpServletRequest request){
        // 不管是注册用户还是绑订用户，都会从页面拿到一个用户唯一标识
        String userId = user.getUsername();
        // 这里可以进行业务上的注册或者绑订逻辑，完成之后执行下面,将业务用户id与social用户的几个id（openid等）联系在一起
        // 即，一起放入spring social的userconnection表中。便于之后用户再次采用第三方登录，可以从这个表中拿到业务的用户id，从而
        // 拿到业务的用户数据放入UserDetails中也即构建Authentication
        appSignUpUtils.doPostSignUp(new ServletWebRequest(request),userId);
    }

    @GetMapping("/me")
    public Object getSecurityContext(Authentication authentication){
        //return SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @GetMapping("/me3")
    public Object getJwtInfo(Authentication authentication,HttpServletRequest request) throws Exception {
        String header = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(header, "Bearer ");
        Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
                .parseClaimsJws(token).getBody();
        String company = (String)claims.get("company");
        log.info("company:"+company);
        return authentication;
    }

    @GetMapping("/me2")
    public Object getSecurityContext2(@AuthenticationPrincipal UserDetails details){
        return details;
    }

    //@RequestMapping(value = "/user",method = RequestMethod.GET)
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询服务")
    public List<User> getUser(@RequestParam String username){
        List<User> list = new ArrayList<User>();
        list.add(new User());
        list.add(new User());
        list.add(new User());
        return list;
    }

    //@RequestMapping(value = "/user/{id:\\d+}",method = RequestMethod.GET)
    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable String id){
        //throw new UserNotExistException("222");
        User user = new User();
        user.setUsername("tom");
        return user;
    }

    @PostMapping
    @JsonView(User.UserDetailView.class)
    public User creatUser(@Valid @RequestBody User user){
//        if(error.hasErrors()){
//            error.getAllErrors().forEach(e -> System.out.println(e.getDefaultMessage()));
//        }
        System.out.println(user);
        user.setId("1");
        return user;
    }

    @PutMapping
    @JsonView(User.UserDetailView.class)
    public User updateUser(@Valid @RequestBody User user, BindingResult error){
        if(error.hasErrors()){
            error.getAllErrors().forEach(e -> {
//                FieldError fieldError = (FieldError) e;
//                String field = fieldError.getField();
//                System.out.println(field);
                System.out.println(e.getDefaultMessage());
            });
        }
        System.out.println(user);
        user.setId("1");
        return user;
    }

    @DeleteMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public void deleteInfo(@PathVariable String id){
        System.out.println(id);
    }

}
