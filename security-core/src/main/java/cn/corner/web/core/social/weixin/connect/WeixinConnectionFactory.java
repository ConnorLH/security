/**
 *
 */
package cn.corner.web.core.social.weixin.connect;

import cn.corner.web.core.social.weixin.api.WeixinAPI;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * 微信的OAuth2工厂
 * 可以创建出一套微信的OAuth2授权使用的类
 *
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<WeixinAPI> {

	/**
	 * @param appId
	 * @param appSecret
	 */
	public WeixinConnectionFactory(String providerId, String appId, String appSecret) {
		super(providerId, new WeixinServiceProvider(appId, appSecret), new WeixinAdapter());
	}

	/**
	 * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
	 */
	@Override
	protected String extractProviderUserId(AccessGrant accessGrant) {
		if(accessGrant instanceof WeixinAccessGrant) {
			return ((WeixinAccessGrant)accessGrant).getOpenId();
		}
		return null;
	}

	@Override
	public Connection<WeixinAPI> createConnection(AccessGrant accessGrant) {
		return new OAuth2Connection<WeixinAPI>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
				accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
	}

	@Override
	public Connection<WeixinAPI> createConnection(ConnectionData data) {
		return new OAuth2Connection<WeixinAPI>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
	}

	/**
	 * 覆盖以上两个方法其实都是为了要重写这个方法，因为每个请求都是不同的用户，而用户的openId信息封装到WeixinAdapter中的，所以每个用户都要有自己的ApiAdapter，所以这里需要new，不能直接从ConnectionFactory中拿那相同的一个。（PS：这种实现不太好，需要改进）
	 * @param providerUserId
	 * @return
	 */
	private ApiAdapter<WeixinAPI> getApiAdapter(String providerUserId) {
		return new WeixinAdapter(providerUserId);
	}

	private OAuth2ServiceProvider<WeixinAPI> getOAuth2ServiceProvider() {
		return (OAuth2ServiceProvider<WeixinAPI>) getServiceProvider();
	}


}
