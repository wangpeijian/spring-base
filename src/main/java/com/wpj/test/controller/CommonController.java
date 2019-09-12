package com.wpj.test.controller;

import com.wpj.test.base.Result;
import com.wpj.test.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangpejian
 * @date 19-6-10 下午5:12
 */
@Slf4j
@RestController
public class CommonController {


    @Autowired
    CommonService commonService;


    @GetMapping("get-wx-user-info")
    public Result getInfo(String encryptedData, String iv, String code) {
        return new Result<>().ok(commonService.getWxUserInfo( encryptedData,  iv,  code));
    }

}
