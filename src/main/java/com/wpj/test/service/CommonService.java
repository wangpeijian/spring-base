package com.wpj.test.service;

import com.wpj.test.model.WxUserInfo;

/**
 * @author wangpejian
 * @date 19-6-10 下午5:13
 */
public interface CommonService {

    /**
     * 获取微信用户信息
     * @param encryptedData
     * @param iv
     * @param code
     * @return
     */
    WxUserInfo getWxUserInfo(String encryptedData, String iv, String code);

}
