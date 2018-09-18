package cn.corner.web.service;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String greeting(String name) {
        return "hello" + name;
    }
}
