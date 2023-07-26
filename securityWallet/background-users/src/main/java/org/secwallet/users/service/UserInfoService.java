package org.secwallet.users.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.secwallet.commonmodel.dto.AuthLoginDto;
import org.secwallet.commonmodel.entity.UserInfo;
import org.secwallet.commonservice.service.UserOperatingService;
import org.secwallet.core.cache.ICache;
import org.secwallet.core.model.Result;
import org.secwallet.core.model.SecUser;
import org.secwallet.core.security.JwtAuthenticatioToken;
import org.secwallet.core.service.AbstractService;
import org.secwallet.core.util.DateUtils;
import org.secwallet.core.util.JwtTokenUtils;
import org.secwallet.core.util.MD5Util;
import org.secwallet.core.util.security.SecurityUtil;
import org.secwallet.users.dao.UserInfoMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Service
public class UserInfoService extends AbstractService<UserInfo,Long> {

    @Resource
    private ICache redisService;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private SecurityUtil securityUtil;
    @Resource
    private UserOperatingService userOperatingService;


    public Result login(String mobile, String checkCode, String loginType, String loginPass, HttpServletRequest request) throws UnsupportedEncodingException {
        UserInfo userInfo = userInfoMapper.findUserInfoByPhone(mobile);

        AuthLoginDto dto = new AuthLoginDto();
        dto.setAvatarAddress(userInfo.getHeadImage());
        dto.setNickName(userInfo.getNickName());
        dto.setUserId(userInfo.getId());
        SecUser user = new SecUser();
        user.setId(userInfo.getId());
        user.setLogin_password(userInfo.getLoginPassword());
        user.setTimesatamp(DateUtils.getTimeStamp());
        user.setMobile(userInfo.getMobile());
        JwtAuthenticatioToken token = securityUtil.login(request, user, userInfo.getLoginPassword(), authenticationManager);
        int num = userInfoMapper.updateUserToken(userInfo.getId(), token.getToken());
        if(num > 0){
            redisService.set("userToken:"+userInfo.getId(),token.getToken(),2592000l);
            dto.setToken(token.getToken());
            dto.setSalt(MD5Util.MD5(UUID.randomUUID().toString()));
        }else {
            return Result.fail("login error",22);
        }
        JSONObject jsonObject = new JSONObject();
        String addr = request.getRemoteAddr();
        String os = JwtTokenUtils.getOS(request);
        jsonObject.put("userId",userInfo.getId());
        jsonObject.put("type",0);
        jsonObject.put("desc","login");
        jsonObject.put("ip",addr);
        jsonObject.put("os",os);
        jsonObject.put("address",null);
        userOperatingService.addUserOperating(jsonObject);
        return Result.ok(dto,"success");
    }

}
