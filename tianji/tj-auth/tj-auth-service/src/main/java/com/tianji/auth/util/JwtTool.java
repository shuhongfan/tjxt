package com.tianji.auth.util;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.tianji.auth.common.constants.AuthErrorInfo;
import com.tianji.auth.common.constants.JwtConstants;
import com.tianji.common.domain.dto.LoginUserDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.utils.AssertUtils;
import com.tianji.common.utils.BooleanUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.common.utils.UserContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Date;

import static com.tianji.auth.common.constants.JwtConstants.JWT_REFRESH_TTL;
import static com.tianji.auth.common.constants.JwtConstants.JWT_TOKEN_TTL;

@Component
public class JwtTool {
    private final StringRedisTemplate stringRedisTemplate;
    private final JWTSigner jwtSigner;

    public JwtTool(StringRedisTemplate stringRedisTemplate, KeyPair keyPair) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    /**
     * 创建 access-token
     *
     * @param userDTO 用户信息
     * @return access-token
     */
    public String createToken(LoginUserDTO userDTO) {
        // 1.生成jws
        return JWT.create()
                .setPayload(JwtConstants.PAYLOAD_USER_KEY, userDTO)
                .setExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_TTL.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    /**
     * 创建刷新token，并将token的JTI记录到Redis中
     *
     * @param userDetail 用户信息
     * @return 刷新token
     */
    public String createRefreshToken(LoginUserDTO userDetail) {
        // 1.生成 JTI
        String jti = UUID.randomUUID().toString(true);
        // 2.生成jwt
        // 2.1.如果是记住我，则有效期7天，否则30分钟
        Duration ttl = BooleanUtils.isTrue(userDetail.getRememberMe()) ?
                JwtConstants.JWT_REMEMBER_ME_TTL : JWT_REFRESH_TTL;
        // 2.2.生成token
        String token = JWT.create()
                .setJWTId(jti)
                .setPayload(JwtConstants.PAYLOAD_USER_KEY, userDetail)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
        // 3.缓存jti，有效期与token一致，过期或删除JTI后，对应的refresh-token失效
        stringRedisTemplate.opsForValue()
                .set(JwtConstants.JWT_REDIS_KEY_PREFIX + userDetail.getUserId(), jti, ttl);
        return token;
    }

    /**
     * 解析刷新token
     *
     * @param refreshToken 刷新token
     * @return 解析刷新token得到的用户信息
     */
    public LoginUserDTO parseRefreshToken(String refreshToken) {
        // 1.校验token是否为空
        AssertUtils.isNotNull(refreshToken, AuthErrorInfo.Msg.INVALID_TOKEN);
        // 2.校验并解析jwt
        JWT jwt;
        try {
            jwt = JWT.of(refreshToken).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN, e);
        }
        // 2.校验jwt是否有效
        if (!jwt.verify()) {
            // 验证失败
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }
        // 3.校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new BadRequestException(400, AuthErrorInfo.Msg.EXPIRED_TOKEN);
        }
        // 4.数据格式校验
        Object userPayload = jwt.getPayload(JwtConstants.PAYLOAD_USER_KEY);
        Object jtiPayload = jwt.getPayload(JwtConstants.PAYLOAD_JTI_KEY);
        if (jtiPayload == null || userPayload == null) {
            // 数据为空
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }

        // 5.数据解析
        LoginUserDTO userDTO;
        try {
            userDTO = ((JSONObject) userPayload).toBean(LoginUserDTO.class);
        } catch (RuntimeException e) {
            // 数据格式有误
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }

        // 6.JTI校验
        String jti = stringRedisTemplate.opsForValue().get(JwtConstants.JWT_REDIS_KEY_PREFIX + userDTO.getUserId());
        if (!StringUtils.equals(jti, jtiPayload.toString())) {
            // jti不一致
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }
        return userDTO;
    }

    /**
     * 清理刷新refresh-token的jti，本质是refresh-token作废
     */
    public void cleanJtiCache() {
        stringRedisTemplate.delete(JwtConstants.JWT_REDIS_KEY_PREFIX + UserContext.getUser());
    }
}
