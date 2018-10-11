package cn.corner.web.app;

import cn.corner.web.core.properties.OAuth2ClientProperties;
import cn.corner.web.core.properties.SecurityProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jetty.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Oauth2认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class MyAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private TokenStore tokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    @Qualifier("jwtTokenEnhancer")
    private TokenEnhancer tokenEnhancer;

    /**
     * 认证服务器的Endpoints配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore);
        if(jwtAccessTokenConverter != null && tokenEnhancer!=null){
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(tokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);

            endpoints
                    .tokenEnhancer(enhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    /**
     * 客户端配置（就像我们到QQ上注册自己的应用，会生成clientId，clientSecret等关于你这个应用的信息）
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 从配置文件读取client的配置信息后存到内存中即可（因为这些注册的应用一般都是我们自己开发的不会很多，又不是像QQ提供开放注册）
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if(ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())){
            for(OAuth2ClientProperties config : securityProperties.getOauth2().getClients()){
                // 这些都是可以在配置文件给每个客户端单独配置的，这里就统一写死了
                builder.withClient(config.getClientId())
                        .secret(config.getClientSecret())
                        .accessTokenValiditySeconds(config.getAccessTokenValiditySeconds())
                        // 该client支持使用的Oauth2认证模式
                        .authorizedGrantTypes("authorization_code","refresh_token","password","client_credentials")
                        .refreshTokenValiditySeconds(2592000)
                        // 该client的拥有的权限范围，其申请的（传参过来）范围不能超过这个范围
                        .scopes("all","read","write")
                        // 这里就统一重定向地址了
                        .redirectUris("http://example.com");
            }
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
        super.configure(security);
    }
}
