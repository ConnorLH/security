package cn.corner.web.core.social.qq.connect;

import cn.corner.web.core.social.qq.api.QQAPI;
import cn.corner.web.core.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 将第三方的用户信息格式化为Spring-Social的用户信息格式
 * 即QQUserInfo ——> Connection
 */
public class QQAdapter implements ApiAdapter<QQAPI> {
    @Override
    public boolean test(QQAPI qqapi) {
        return true;
    }

    @Override
    public void setConnectionValues(QQAPI qqapi, ConnectionValues connectionValues) {
        QQUserInfo userInfo = qqapi.getUserInfo();
        connectionValues.setDisplayName(userInfo.getNickname());
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());
        connectionValues.setProfileUrl(null);
        connectionValues.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQAPI qqapi) {
        return null;
    }

    /**
     * 用于像微博这种更新用户状态
     * @param qqapi
     * @param s
     */
    @Override
    public void updateStatus(QQAPI qqapi, String s) {

    }
}
