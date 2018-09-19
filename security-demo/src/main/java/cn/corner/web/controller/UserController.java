package cn.corner.web.controller;

import cn.corner.web.dto.User;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public Object getSecurityContext(Authentication authentication){
        //return SecurityContextHolder.getContext().getAuthentication();
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
