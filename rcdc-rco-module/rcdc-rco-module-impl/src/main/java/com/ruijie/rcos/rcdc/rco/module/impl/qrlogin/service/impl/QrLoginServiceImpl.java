package com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service.QrLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: QrLoginService
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年5月6日
 *
 * @author zhang.zhiwen
 */
@Service
public class QrLoginServiceImpl implements QrLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrLoginServiceImpl.class);

    private static final Cache<String, String> CACHE = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();

    private static final String PRE_FLAG = "QR_LOGIN_";

    @Autowired
    protected RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Override
    public void saveQrLoginId(UUID id, String userName) {
        Assert.notNull(id, "id can not null");
        Assert.hasText(userName, "userName can not null");
        // 存储用户缓存
        CACHE.put(PRE_FLAG + id, userName);
    }

    @Override
    public ShineLoginDTO qryLoginDto(String id, @Nullable String userName) throws BusinessException {
        Assert.notNull(id, "id can not null");
        userName = userName == null ? CACHE.getIfPresent(PRE_FLAG + id) : userName;

        // phone对应了用户名
        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(userName);
        ShineLoginDTO shineLoginDTO = new ShineLoginDTO();
        shineLoginDTO.setUserName(userName);
        if (cbbUserDetailDTO == null) {
            LOGGER.info("数据库中不存在用户[{}]", userName);
            return shineLoginDTO;
        }
        shineLoginDTO.setPassword(cbbUserDetailDTO.getPassword());
        return shineLoginDTO;
    }
}
