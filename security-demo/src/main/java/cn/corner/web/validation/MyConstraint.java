package cn.corner.web.validation;

import cn.corner.web.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 实现该接口后spring会自动创建bean
 */
public class MyConstraint implements ConstraintValidator<MyValidate,Object> {

    @Autowired
    private HelloService service;

    @Override
    public void initialize(MyValidate constraintAnnotation) {
        System.out.println("my validate run");
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(value);
        service.greeting("tom");
        return false;
    }
}
