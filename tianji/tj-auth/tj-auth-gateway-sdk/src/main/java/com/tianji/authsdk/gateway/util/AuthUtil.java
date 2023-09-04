package com.tianji.authsdk.gateway.util;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import com.tianji.auth.common.domain.PrivilegeRoleDTO;
import com.tianji.common.domain.R;
import com.tianji.common.domain.dto.LoginUserDTO;
import com.tianji.common.exceptions.ForbiddenException;
import com.tianji.common.exceptions.UnauthorizedException;
import com.tianji.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.stream.Collectors;

import static com.tianji.auth.common.constants.AuthErrorInfo.Code.EXPIRED_TOKEN_CODE;
import static com.tianji.auth.common.constants.AuthErrorInfo.Code.INVALID_TOKEN_CODE;
import static com.tianji.auth.common.constants.AuthErrorInfo.Msg.*;
import static com.tianji.auth.common.constants.JwtConstants.*;

@Slf4j
public class AuthUtil {
    // 缓存权限信息
    private Map<String, PrivilegeRoleDTO> privileges = new HashMap<>();
    // 要拦截的路径匹配符的集合
    private Set<String> paths = new HashSet<>();
    // 权限版本信息，减少不必要的缓存处理
    private int privilegeVersion;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final JwtSignerHolder jwtSignerHolder;
    private final StringRedisTemplate stringRedisTemplate;
    private final BoundHashOperations<String, String, String> hashOps;

    public AuthUtil(JwtSignerHolder jwtSignerHolder, StringRedisTemplate stringRedisTemplate) {
        this.jwtSignerHolder = jwtSignerHolder;
        this.stringRedisTemplate = stringRedisTemplate;
        this.hashOps = stringRedisTemplate.boundHashOps(AUTH_PRIVILEGE_KEY);
    }

    public R<LoginUserDTO> parseToken(String token) {
        // 1.校验token是否为空
        if(StringUtils.isBlank(token)){
            return R.error(INVALID_TOKEN_CODE, INVALID_TOKEN);
        }
        JWT jwt = null;
        try {
            jwt = JWT.of(token).setSigner(jwtSignerHolder.getJwtSigner());
        } catch (Exception e) {
            return R.error(INVALID_TOKEN_CODE, INVALID_TOKEN);
        }
        // 2.校验jwt是否有效
        if (!jwt.verify()) {
            // 验证失败，返回空
            return R.error(INVALID_TOKEN_CODE, INVALID_TOKEN);
        }
        // 3.校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            return R.error(EXPIRED_TOKEN_CODE, EXPIRED_TOKEN);
        }
        // 4.数据格式校验
        Object userPayload = jwt.getPayload(PAYLOAD_USER_KEY);
        if (userPayload == null) {
            // 数据为空
            return R.error(INVALID_TOKEN_CODE, INVALID_TOKEN_PAYLOAD);
        }

        // 5.数据解析
        LoginUserDTO userDTO;
        try {
            userDTO = ((JSONObject)userPayload).toBean(LoginUserDTO.class);
        } catch (RuntimeException e) {
            // token格式有误
            return R.error(INVALID_TOKEN_CODE, INVALID_TOKEN_PAYLOAD);
        }

        // 6.返回
        return R.ok(userDTO);
    }

    public void checkAuth(String antPath, R<LoginUserDTO> r){
        // 1.判断是否是需要权限的路径
        String matchPath = findMatchPath(antPath);
        if(matchPath == null){
            // 没有权限限制，直接放行
            return;
        }
        // 2.判断是否登录成功
        if(!r.success()){
            // 未登录，直接报错
            throw new UnauthorizedException(r.getCode(), r.getMsg());
        }
        // 3.获取当前路径所需权限
        PrivilegeRoleDTO pathPrivilege = findPathPrivilege(matchPath);

        // 4.权限判断
        Set<Long> requiredRoles = pathPrivilege.getRoles();
        if (!CollectionUtil.contains(requiredRoles, r.getData().getRoleId())) {
            // 没有访问权限
            throw new ForbiddenException(FORBIDDEN);
        }
    }

    private String findMatchPath(String antPath){
        String matchPath = null;
        for (String pathPattern : paths) {
            if(antPathMatcher.match(pathPattern, antPath)){
                matchPath = pathPattern;
                break;
            }
        }
        return matchPath;
    }

    private PrivilegeRoleDTO findPathPrivilege(String path){
        return privileges.get(path);
    }

    private List<PrivilegeRoleDTO> loadPrivileges(){
        List<String> values = hashOps.values();
        if(CollUtil.isEmpty(values)){
            return Collections.emptyList();
        }
        return values.stream()
                .map(json -> JSONUtil.toBean(json, PrivilegeRoleDTO.class))
                .collect(Collectors.toList());
    }

    private int currentVersion() {
        String version = stringRedisTemplate.opsForValue().get(AUTH_PRIVILEGE_VERSION_KEY);
        if(StrUtil.isEmpty(version)){
            return 0;
        }
        return Integer.parseInt(version);
    }


    @Scheduled(fixedDelay = 20000)
    public void refreshTask(){
        // 1.获取版本号
        int currentVersion = currentVersion();
        if (currentVersion == this.privilegeVersion) {
            // 版本一致，说明数据没有更新，直接结束任务
            return;
        }
        // 2.获取最新权限信息
        List<PrivilegeRoleDTO> privilegeRoleDTOS = loadPrivileges();
        if(CollUtil.isEmpty(privilegeRoleDTOS)){
            // 更新版本
            this.privilegeVersion = currentVersion;
            return;
        }
        // 3.数据处理
        Map<String, PrivilegeRoleDTO> map = new HashMap<>();
        for (PrivilegeRoleDTO p : privilegeRoleDTOS) {
            map.put(p.getAntPath(), p);
            this.privileges = map;
        }
        this.paths = map.keySet();
        // 4.更新版本
        this.privilegeVersion = currentVersion;
    }
}
