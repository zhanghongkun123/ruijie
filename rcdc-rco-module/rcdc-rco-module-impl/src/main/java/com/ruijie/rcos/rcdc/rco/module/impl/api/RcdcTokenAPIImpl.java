package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacLoginAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcdcTokenAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TokenContextDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.VerifyAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAdminPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.RccmAdminTokenCache;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TokenContextRegistry;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/17
 *
 * @author jarman
 */
public class RcdcTokenAPIImpl implements RcdcTokenAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(RcdcTokenAPIImpl.class);


    @Autowired
    private TokenContextRegistry tokenContextRegistry;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private RccmAdminTokenCache tokenCache;

    @Autowired
    private AdminManageAPI adminManageAPI;
    

    private static final String ADMIN_NAME = "admin";

    private static final String LOCK_PREFIX = "rccm_apply_token_lock_prefix_";

    private static final int LOCK_TIME = 3;

    @Override
    public UUID checkLoginToken(UUID token) throws BusinessException {
        Assert.notNull(token, "token request cannot be null!");
        TokenContextDTO tokenContextDTO = tokenContextRegistry.findToken(token);
        if (tokenContextDTO == null) {
            throw new BusinessException(AaaBusinessKey.RCDC_RCO_ADMIN_LOGIN_TOKEN_ERROR);
        }
        if (tokenContextDTO.getInvalidDate().isBefore(LocalDateTime.now())) {
            tokenContextRegistry.removeToken(token);
            throw new BusinessException(AaaBusinessKey.RCDC_RCO_ADMIN_LOGIN_TOKEN_ERROR);
        }
        tokenContextRegistry.removeToken(token);
        return tokenContextDTO.getAdminId();
    }

    @Override
    public String applyToken(VerifyAdminRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        AtomicReference<String> tokenReference = new AtomicReference<>();
        LockableExecutor.executeWithTryLock(LOCK_PREFIX + request.getUserName(), () -> tokenReference.set(doApplyToken(request)), LOCK_TIME);
        return tokenReference.get();
    }

    private String doApplyToken(VerifyAdminRequest request) throws BusinessException {
        // 兼容rcenter旧版本处理
        if (request.getSessionId() == null) {
            LOGGER.info("rcenter请求sessionId为空，兼容rcenter旧版本，直接返回登录接口token");
            return loginAdmin(request).getToken();
        }

        String adminName = request.getUserName();
        RccmAdminTokenCache.CacheInfo cache = tokenCache.getCache(adminName, request.getSubSystem());
        LOGGER.info("查询管理员[{}]缓存信息[{}]", adminName, JSON.toJSONString(cache));

        if (Objects.isNull(cache)) {
            LOGGER.debug("管理员[{}]未绑定token，生成新的token", adminName);
            return loginAndPutCache(request);
        }

        if (!request.getSessionId().equals(cache.getSessionId()) || StringUtils.isEmpty(cache.getIacToken())) {
            LOGGER.debug("管理员[{}]会话id变更或iacToken无效，生成新的token", adminName);
            return loginAndPutCache(request);
        }

        try {
            baseAdminMgmtAPI.getAdminByToken(cache.getIacToken());
        } catch (BusinessException e) {
            // 如果管理员token已经过期，调用登录接口生成新的token
            if (BusinessKey.RCDC_RCO_SK_NOT_LOGIN_EXCEPTION_KEY.equals(e.getKey())) {
                LOGGER.warn("管理员[{}]iacToken[{}]已过期，生成新的token", adminName, cache.getIacToken());
                return loginAndPutCache(request);
            }
            // 其他异常直接抛出
            throw e;
        }
        return cache.getIacToken();
    }

    /**
     * 调用登录接口生成iacToken，并且将iacToken加入缓存中
     *
     * @param request 请求
     * @return iacToken
     */
    private String loginAndPutCache(VerifyAdminRequest request) throws BusinessException {
        IacAdminDTO iacAdminDTO = loginAdmin(request);
        RccmAdminTokenCache.CacheInfo cacheInfo = new RccmAdminTokenCache.CacheInfo(request.getSessionId(), iacAdminDTO.getToken());
        tokenCache.addCache(request.getUserName(), request.getSubSystem(), cacheInfo);
        return iacAdminDTO.getToken();
    }

    private IacAdminDTO loginAdmin(VerifyAdminRequest request) throws BusinessException {
        IacLoginAdminRequest loginRequest = new IacLoginAdminRequest();
        loginRequest.setSubSystem(request.getSubSystem());
        loginRequest.setLoginIp(request.getLoginIp());
        loginRequest.setLoginName(request.getUserName());
        // 不需要图形验证码
        loginRequest.setDeviceId(UUID.randomUUID().toString());
        if (StringUtils.isBlank(request.getLoginIp())) {
            loginRequest.setIgnoreIpWhitelistCheck(true);
        }
        if (isSupperAdmin(request)) {
            // 免密登录，调用获取密码接口
            IacAdminDTO iacAdminDTO = adminMgmtAPI.getAdminByUserName(request.getUserName());
            GetAdminPasswordResponse adminPasswordResponse = adminManageAPI.getAdminPassword(new IdRequest(iacAdminDTO.getId()));
            loginRequest.setPwd(adminPasswordResponse.getPassword());
            return baseAdminMgmtAPI.loginAdminNoAuth(loginRequest);
        }
        loginRequest.setPwd(request.getPassword());

        return baseAdminMgmtAPI.loginAdminNoAuth(loginRequest);
    }

    private boolean isSupperAdmin(VerifyAdminRequest request) {
        return ADMIN_NAME.equals(request.getUserName()) && request.getHasSuper() != null && request.getHasSuper();
    }
}
