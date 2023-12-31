/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.secwallet.core.util;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.*;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Jwt token tool
 *
 * @author wfnuser
 */
@Component
public class JwtTokenUtils {

    private final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    private static final String AUTHORITIES_KEY = "auth";

    /**
     * secret key
     */
    private String secretKey;

    /**
     * Token validity time(ms)
     */
    private long tokenValidityInMilliseconds;

    @PostConstruct
    public void init() {
        this.secretKey = "SecretKey012345678901234567890123456789012345678901234567890123456789";
        this.tokenValidityInMilliseconds = 1000 * 60 * 30L;
    }

    /**
     * Create token
     *
     * @param
     * @return token
     */
    public String createToken(String txt) {
        /**
         * Current time
         */
        long now = (new Date()).getTime();
        /**
         * Validity date
         */
        Date validity;
        validity = new Date(now + this.tokenValidityInMilliseconds);

        /**
         * create token
         */
        return Jwts.builder()
                .setSubject(txt)
                .claim(AUTHORITIES_KEY, "")
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Create token
     *
     * @param authentication auth info
     * @return token
     */
    public String createToken(Authentication authentication) {
        /**
         * Current time
         */
        long now = (new Date()).getTime();
        /**
         * Validity date
         */
        Date validity;
        validity = new Date(now + this.tokenValidityInMilliseconds);

        /**
         * create token
         */
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, "")
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Get auth Info
     *
     * @param token token
     * @return auth info
     */
    public Authentication getAuthentication(String token) {
        /**
         *  parse the payload of token
         */
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));


        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * validate token
     *
     * @param token token
     * @return whether valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    public static long getHeadUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            String s = AesUtil.aesDecryptCbc(token);
            JSONObject jsonObject1 = JSONObject.parseObject(s);
            long userId = jsonObject1.getLong("id");
            return userId;
        }
        return 0;
    }

    public static String getHeadImei(HttpServletRequest request) {
        String version = request.getHeader("version-info");

        if (version != null) {
            JSONObject jsonObject = JSONObject.parseObject(version);
            return jsonObject.getString("imei");
        }
        return "";
    }

    public static int getVersion(HttpServletRequest request) {
        String version = request.getHeader("version-info");

        if (!TextUtils.isEmpty(version)) {
            JSONObject jsonObject = JSONObject.parseObject(version);

            String appVersion = jsonObject.getString("appVersion");

            return Integer.parseInt(appVersion);
        }

        return 0;
    }
    public static String getOS(HttpServletRequest request) {
        String version = request.getHeader("version-info");

        if (!TextUtils.isEmpty(version)) {
            JSONObject jsonObject = JSONObject.parseObject(version);

            String appVersion = jsonObject.getString("os");
            return appVersion;
        }
        return "";
    }
    public static Map getHeader(HttpServletRequest request) {
        Map header = new HashMap();
        String version = request.getHeader("version-info");
        String token = request.getHeader("Authorization");
        header.put("version-info",version);
        header.put("Authorization",token);
        return header;
    }

    public static long getUserId(String token) {
        if (token != null) {
            String s = AesUtil.aesDecryptCbc(token);
            JSONObject jsonObject1 = JSONObject.parseObject(s);
            long userId = jsonObject1.getLong("id");
            return userId;
        }
        return 0;
    }
}
