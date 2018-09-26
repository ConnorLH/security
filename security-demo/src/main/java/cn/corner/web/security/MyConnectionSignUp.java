package cn.corner.web.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * 根据业务选择性实现，作用是在social获取到用户信息后默认调用这个类去创建一个用户放入业务数据库中（自己代码实现）
 * 并且放入userconnection表中
 */
@Component
public class MyConnectionSignUp implements ConnectionSignUp {

    /**
     * 根据connection中的信息创建默认用户并返回用户唯一标识
     * @param connection
     * @return
     */
    @Override
    public String execute(Connection<?> connection) {
        return connection.getDisplayName();
    }
}
