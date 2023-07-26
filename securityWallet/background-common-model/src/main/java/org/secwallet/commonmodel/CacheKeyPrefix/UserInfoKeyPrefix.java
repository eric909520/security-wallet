package org.secwallet.commonmodel.CacheKeyPrefix;

import org.secwallet.commonmodel.enums.KeyPrefixEnum;
import org.secwallet.core.cache.BasePrefix;

/**
 * User Information Cache Prefix
 */
public class UserInfoKeyPrefix extends BasePrefix {
    private UserInfoKeyPrefix(Long expireSeconds, KeyPrefixEnum prefix){
        super(expireSeconds,prefix.toString());
    }

    public static  UserInfoKeyPrefix registerCode = new UserInfoKeyPrefix(600L, KeyPrefixEnum.userRegisterCheckCode);

}
