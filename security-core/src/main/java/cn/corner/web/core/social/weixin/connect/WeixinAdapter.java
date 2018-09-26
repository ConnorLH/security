/**
 *
 */
package cn.corner.web.core.social.weixin.connect;

import cn.corner.web.core.social.weixin.api.WeixinAPI;
import cn.corner.web.core.social.weixin.api.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;


/**
 * 微信 api适配器，将微信 api的数据模型转为spring social的标准模型。
 *
 *
 * @author zhailiang
 *
 */
public class WeixinAdapter implements ApiAdapter<WeixinAPI> {

	private String openId;

	public WeixinAdapter() {}

	public WeixinAdapter(String openId){
		this.openId = openId;
	}

	/**
	 * @param api
	 * @return
	 */
	@Override
	public boolean test(WeixinAPI api) {
		return true;
	}

	/**
	 * @param api
	 * @param values
	 */
	@Override
	public void setConnectionValues(WeixinAPI api, ConnectionValues values) {
		WeixinUserInfo profile = api.getUserInfo(openId);
		values.setProviderUserId(profile.getOpenid());
		values.setDisplayName(profile.getNickname());
		values.setImageUrl(profile.getHeadimgurl());
	}

	/**
	 * @param api
	 * @return
	 */
	@Override
	public UserProfile fetchUserProfile(WeixinAPI api) {
		return null;
	}

	/**
	 * @param api
	 * @param message
	 */
	@Override
	public void updateStatus(WeixinAPI api, String message) {
		//do nothing
	}

}
