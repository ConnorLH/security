package cn.corner.web.app.conf;

import cn.corner.web.app.jwt.MyJwtTokenEnhancer;
import cn.corner.web.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenStoreConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @ConditionalOnProperty(prefix = "corner.security.oauth2",name = "storeType",havingValue = "redis")
    public TokenStore redisTokenStore(){
        return new MyRedisTokenStore(redisConnectionFactory);
    }

    @Configuration
    @ConditionalOnProperty(prefix = "corner.security.oauth2",name = "storeType",havingValue = "jwt",matchIfMissing = true)
    public static class JwtTokenConfig{

        @Autowired
        private SecurityProperties securityProperties;

        /**
         * JWT存储
         * @return
         */
        @Bean
        public TokenStore jwtTokenStore(){
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        /**
         * JWT生成
         * @return
         */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
            accessTokenConverter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
            return accessTokenConverter;
        }

        @Bean
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer(){
            return new MyJwtTokenEnhancer();
        }
    }
}
