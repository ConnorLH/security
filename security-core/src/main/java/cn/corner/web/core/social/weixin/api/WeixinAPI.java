package cn.corner.web.core.social.weixin.api;

import cn.corner.web.core.social.qq.api.QQUserInfo;

public interface WeixinAPI {

    WeixinUserInfo getUserInfo(String openId);
}
