package cn.corner.web.app.social;

import cn.corner.web.app.AppSecretException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * 因为app不是基于session的需要自己操作会话信息存储，这里使用redis进行存储
 */
@Component
public class AppSignUpUtils {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    public void saveConnectionData(WebRequest request, ConnectionData connectionData){
        redisTemplate.opsForValue().set(getKey(request),connectionData,10, TimeUnit.MINUTES);
    }

    /**
     * 绑定逻辑，将之前保存的social信息取出来和申请注册的userId一起插入到usersConnection表中
     * 这样用户下一次使用social登录就可以找到userId了，进而找到用户的业务信息，这样就可以认证成功了
     * @param request
     * @param userId
     */
    public void doPostSignUp(WebRequest request,String userId){
        String key = getKey(request);
        if(!redisTemplate.hasKey(key)){
            throw new AppSecretException("无法找到缓存的用户社交账号信息");
        }
        ConnectionData connectionData = (ConnectionData)redisTemplate.opsForValue().get(key);
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
        Connection<?> connection = connectionFactory.createConnection(connectionData);
        usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);
        redisTemplate.delete(key);
    }

    private String getKey(WebRequest request) {
        String deviceId = request.getHeader("deviceId");
        if(StringUtils.isBlank(deviceId)){
            throw new AppSecretException("设备id参数不能为空");
        }
        return "corner:security:social:connect:"+deviceId;
    }
}
