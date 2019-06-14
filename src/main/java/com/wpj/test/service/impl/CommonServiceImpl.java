package com.wpj.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wpj.test.config.WxConfig;
import com.wpj.test.model.WxUserInfo;
import com.wpj.test.service.CommonService;
import com.wpj.test.util.AesCbcUtil;
import com.wpj.test.util.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangpejian
 * @date 19-6-10 下午5:13
 */
@Service
public class CommonServiceImpl implements CommonService {

    private final String JS_CODE_2_SESSION = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    WxConfig wxConfig;

    @Override
    public WxUserInfo getWxUserInfo(String encryptedData, String iv, String code) {
        WxUserInfo userInfo = new WxUserInfo();

        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
        //请求参数
        String params = "?appid=" + wxConfig.getAppId() + "&secret=" + wxConfig.getSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        //发送请求
        String str = HttpRequest.get(JS_CODE_2_SESSION + params);
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(str);
        //获取会话密钥（session_key）
        String sessionKey = json.get("session_key").toString();
        //用户的唯一标识（openid）
        String openid = (String) json.get("openid");

        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                JSONObject userInfoJSON = JSONObject.parseObject(result);
                userInfo.setNickName((String) userInfoJSON.get("nickName"));
                userInfo.setOpenId((String) userInfoJSON.get("openId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
