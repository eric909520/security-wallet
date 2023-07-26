package org.secwallet.users.api;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.secwallet.commonmodel.dto.Body;
import org.secwallet.core.annontation.AuthIgnore;
import org.secwallet.core.controller.BaseController;
import org.secwallet.core.model.Result;
import org.secwallet.core.util.AesUtil;
import org.secwallet.users.service.UserInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
@Api(value = "userCenter", tags = "userCenter")
@RestController
@RequestMapping("/api/user")
public class UserInfoController extends BaseController {

    @Resource
    private UserInfoService userInfoService;

    @AuthIgnore
    @ApiOperation(value = "login")
    @PostMapping("login")
    public Result login(@RequestBody Body requestBody, HttpServletRequest request) {
        String data = AesUtil.aesDecryptCbc(requestBody.getObject());
        JSONObject json = JSONObject.parseObject(data);
        String mobile = json.getString("mobile");
        String checkCode = json.getString("checkCode");
        String loginType = json.getString("loginType");
        String loginPass = json.getString("loginPass");
        try {
            return userInfoService.login(mobile, checkCode, loginType, loginPass, request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Result.fail("login error", 13);
        }
    }

}
