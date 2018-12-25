/**
 *
 */
package cn.corner.web.core.social.weixin.connect;

import cn.corner.web.core.social.weixin.api.WeixinAPI;
import cn.corner.web.core.social.weixin.api.WeixinAPIImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;

/**
 *
 * 微信的OAuth2流程处理器的提供器，供spring social的connect体系调用
 *
 * @author zhailiang
 *
 */
public class WeixinServiceProvider extends AbstractOAuth2ServiceProvider<WeixinAPI> {

	/**
	 * 微信获取授权码的url
	 */
	private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
	/**
	 * 微信获取accessToken的url
	 */
	private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

	/**
	 * @param appId
	 * @param appSecret
	 */
	public WeixinServiceProvider(String appId, String appSecret) {
		super(new WeixinOauth2Template(appId, appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
	}

	@Override
	public WeixinAPI getApi(String accessToken) {
		// 这里直接从WeixinOauth2Template中获取到的
		OAuth2Operations oAuthOperations = getOAuthOperations();
		return new WeixinAPIImpl(accessToken);
	}

}
