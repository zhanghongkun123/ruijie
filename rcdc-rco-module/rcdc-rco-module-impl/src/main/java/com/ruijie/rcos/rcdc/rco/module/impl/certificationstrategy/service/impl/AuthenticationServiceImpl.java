package com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.AuthenticationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.AuthenticationService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AuthenticationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AuthenticationEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户登录认证信息服务类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/11
 *
 * @author linke
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    /**
     * 锁的前缀
     */
    private static final String LOCK_PREFIX = "GET_RESOURCE_PREFIX_";

    /**
     * 获取锁的等待时间
     */
    private static final int WAIT_TIME = 3;

    @Autowired
    private AuthenticationDAO authenticationDAO;


    @Override
    public void unlock(UUID resourceId, CertificationTypeEnum type) {
        Assert.notNull(resourceId, "userId must not be null");
        Assert.notNull(type, "type must not be null");
        AuthenticationEntity authenticationEntity = authenticationDAO.findByResourceIdAndType(resourceId, type);
        if (authenticationEntity == null) {
            return;
        }
        authenticationEntity.setPwdErrorTimes(0);
        authenticationEntity.setLock(false);
        authenticationEntity.setLockTime(null);
        authenticationEntity.setUnlockTime(null);
        authenticationEntity.setUpdateTime(new Date());
        authenticationDAO.save(authenticationEntity);
    }

    @Override
    public void updateAuthentication(AuthenticationDTO updateUserDTO) {
        Assert.notNull(updateUserDTO, "updateUserDTO must not be null");

        try {
            LockableExecutor.executeWithTryLock(LOCK_PREFIX + updateUserDTO.getResourceId().toString(), () -> {
                AuthenticationEntity userAuthEntity = getOrNewAuthenticationEntity(updateUserDTO.getResourceId(), updateUserDTO.getType());
                userAuthEntity.setPwdErrorTimes(getValue(updateUserDTO.getPwdErrorTimes(), userAuthEntity.getPwdErrorTimes()));
                userAuthEntity.setLock(getValue(updateUserDTO.getLock(), userAuthEntity.getLock()));
                userAuthEntity.setLockTime(getValue(updateUserDTO.getLockTime(), userAuthEntity.getLockTime()));
                userAuthEntity.setUnlockTime(getValue(updateUserDTO.getUnlockTime(), userAuthEntity.getUnlockTime()));
                userAuthEntity.setUpdatePasswordTime(getValue(updateUserDTO.getUpdatePasswordTime(), userAuthEntity.getUpdatePasswordTime()));
                userAuthEntity.setLastLoginTime(getValue(updateUserDTO.getLastLoginTime(), userAuthEntity.getLastLoginTime()));
                userAuthEntity.setUpdateTime(new Date());

                authenticationDAO.save(userAuthEntity);
            }, WAIT_TIME);
        } catch (BusinessException e) {
            LOGGER.error(String.format("获取资源[%s]的对应的锁失败，失败原因：", updateUserDTO.getResourceId()), e);
        }

    }

    @Override
    public void deleteByResourceId(UUID resourceId, CertificationTypeEnum type) {
        Assert.notNull(resourceId, "userId must not be null");
        Assert.notNull(type, "type must not be null");
        authenticationDAO.deleteByResourceIdAndType(resourceId, type);
    }

    @Override
    public boolean getLockedStatusByIdAndType(UUID resourceId, CertificationTypeEnum type) {
        Assert.notNull(resourceId, "userId must not be null");
        Assert.notNull(type, "type must not be null");
        AuthenticationEntity entity = authenticationDAO.findByResourceIdAndType(resourceId, type);
        return BooleanUtils.isTrue(entity.getLock());
    }

    private AuthenticationEntity getOrNewAuthenticationEntity(UUID resourceId, CertificationTypeEnum type) {

        AuthenticationEntity authenticationEntity = authenticationDAO.findByResourceIdAndType(resourceId, type);
        if (authenticationEntity != null) {
            return authenticationEntity;
        }

        Date createTime = new Date();
        authenticationEntity = new AuthenticationEntity();
        authenticationEntity.setLastLoginTime(createTime);
        authenticationEntity.setPwdErrorTimes(0);
        authenticationEntity.setResourceId(resourceId);
        authenticationEntity.setType(type);
        authenticationEntity.setLock(false);
        authenticationEntity.setCreateTime(createTime);
        authenticationEntity.setUpdatePasswordTime(createTime);

        return authenticationEntity;
    }

    /**
     * 获取值
     *
     * @param nullableObj 首选值
     * @param defaultObj  首选值为null时的默认值
     * @return Object
     */
    private <T> T getValue(T nullableObj, T defaultObj) {
        return Optional.ofNullable(nullableObj).orElse(defaultObj);
    }

}
