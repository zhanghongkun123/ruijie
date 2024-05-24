package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.utils.UserLicenseHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.dao.UserLicenseDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.dao.UserSessionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity.UserLicenseEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity.UserSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.enums.ClearSessionReasonTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.service.UserLicenseService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.tx.UserLicenseServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.license.module.def.LicenseAware;
import com.ruijie.rcos.rcdc.license.module.def.annotation.LicenseTarget;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbLicenseChangeDTO;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbLicenseClassifiedUsageDTO;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseTargetEnum;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户授权事务处理实现类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
@Service
@LicenseTarget(value = CbbLicenseTargetEnum.USER)
public class UserLicenseServiceImpl implements LicenseAware, UserLicenseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLicenseServiceImpl.class);

    @Autowired
    private UserLicenseDAO userLicenseDAO;

    @Autowired
    private UserSessionDAO userSessionDAO;

    @Autowired
    private UserLicenseServiceTx userLicenseServiceTx;

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;


    @Override
    public List<CbbLicenseClassifiedUsageDTO> classifyLicenseUsage() {
        // 非用户并发授权模式下，需要判断授权
        if (!cbbLicenseCenterAPI.isActiveUserLicenseMode()) {
            userLicenseDAO.deleteAll();
            userSessionDAO.deleteAll();
        }
        List<CbbLicenseClassifiedUsageDTO> classifiedUsageDTOList = userLicenseDAO.getLicenseClassifiedUsage();
        if (!CollectionUtils.isEmpty(classifiedUsageDTOList)) {
            classifiedUsageDTOList.forEach(classifiedUsageDTO -> classifiedUsageDTO.setLicenseTarget(CbbLicenseTargetEnum.USER));
        }
        return classifiedUsageDTOList;
    }


    @Override
    public void notifyLicenseChanged(CbbLicenseChangeDTO licenseChangeDTO) {
        Assert.notNull(licenseChangeDTO, "licenseChangeDTO can not be null");
        if (licenseChangeDTO.getChangeNum() < 0) {
            // 采用直接回收同类授权的方式处理
            int count = this.clearUserAuthByLicenseTypeAndDuration(licenseChangeDTO.getLicenseTag(), licenseChangeDTO.getLicenseDuration());
            LOGGER.info("批量释放[{}]个[{}-{}]用户授权", count, licenseChangeDTO.getLicenseDuration(), licenseChangeDTO.getLicenseTag());
        }
    }

    @Override
    public boolean releaseLicenseByLicenseTypeAndDuration(String licenseType, CbbLicenseDurationEnum licenseDuration) {
        Assert.hasText(licenseType, "licenseType can not be empty");
        Assert.notNull(licenseDuration, "licenseDuration can not be null");
        int count = this.clearUserAuthByLicenseTypeAndDuration(licenseType, licenseDuration);
        LOGGER.info("批量释放[{}]个[{}-{}]用户授权", count, licenseDuration, licenseType);
        return true;
    }

    @Override
    public int clearUserAuthByLicenseTypeAndDuration(String licenseType, CbbLicenseDurationEnum duration) {
        Assert.hasText(licenseType, "licenseType can not be empty");
        Assert.notNull(duration, "duration can not be null");
        List<UserLicenseEntity> userLicenseEntityList = userLicenseDAO.findByLicenseTypeAndLicenseDuration(licenseType, duration);
        List<ReentrantLock> userLockList = new ArrayList<>();
        List<UUID> userIdList = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(userLicenseEntityList)) {
                userIdList = userLicenseEntityList.stream().map(UserLicenseEntity::getId).collect(Collectors.toList());
                for (UUID userId : userIdList) {
                    ReentrantLock userLock = UserLicenseHelper.getUserLockAlways(userId);
                    userLock.lock();
                    userLockList.add(userLock);
                }
            }
            return userLicenseServiceTx.clearUserAuthByLicenseTypeAndDuration(userIdList, licenseType, duration);
        } finally {
            for (ReentrantLock userLock : userLockList) {
                userLock.unlock();
            }
        }
    }

    @Override
    public UUID createUserSessionAndLicense(UserSessionDTO userSessionDTO) throws BusinessException {
        Assert.notNull(userSessionDTO, "userSessionDTO can not be null");
        UUID userId = userSessionDTO.getUserId();
        ReentrantLock userLock = UserLicenseHelper.getUserLockAlways(userId);
        userLock.lock();
        LOGGER.debug("用户[{}]在此加锁成功", userId);
        try {
            return userLicenseServiceTx.createUserSessionAndLicense(userSessionDTO);
        } finally {
            userLock.unlock();
            LOGGER.debug("用户[{}]在此解锁成功", userId);
        }
    }

    @Override
    public int updateUserSessionAndLicense(UUID userId, String terminalId, TerminalTypeEnum terminalType, List<UserSessionDTO> sessionInfoList)
            throws BusinessException {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.notNull(sessionInfoList, "sessionInfoList can not be null");
        ReentrantLock userLock = UserLicenseHelper.getUserLockAlways(userId);
        userLock.lock();
        LOGGER.debug("用户[{}]在此加锁成功", userId);
        try {
            return userLicenseServiceTx.updateUserSessionAndLicense(userId, terminalId, terminalType, sessionInfoList);
        } finally {
            userLock.unlock();
            LOGGER.debug("用户[{}]在此解锁成功", userId);
        }
    }

    @Override
    public int clearUserSessionAndLicenseByResource(ClearSessionReasonTypeEnum clearType, ResourceTypeEnum resourceType, UUID resourceId) {
        Assert.notNull(clearType, "clearType cannot be null");
        Assert.notNull(resourceType, "resourceType cannot be null");
        Assert.notNull(resourceId, "resourceId cannot be null");
        List<UserSessionEntity> userSessionEntityList = userSessionDAO.findByResourceTypeAndResourceId(resourceType, resourceId);
        int count = 0;
        if (!CollectionUtils.isEmpty(userSessionEntityList)) {
            for (UserSessionEntity userSessionEntity : userSessionEntityList) {
                UUID userId = userSessionEntity.getUserId();
                ReentrantLock userLock = UserLicenseHelper.getUserLockAlways(userId);
                userLock.lock();
                LOGGER.debug("用户[{}]在此加锁成功", userId);
                try {
                    userLicenseServiceTx.clearUserSession(userSessionEntity, clearType);
                    count++;
                } finally {
                    userLock.unlock();
                    LOGGER.debug("用户[{}]在此解锁成功", userId);
                }
            }
        }
        return count;
    }

    @Override
    public int clearUserSessionAndLicenseByTerminal(ClearSessionReasonTypeEnum clearType, TerminalTypeEnum terminalType, String terminalId) {
        Assert.notNull(clearType, "clearType cannot be null");
        Assert.notNull(terminalType, "terminalType cannot be null");
        Assert.hasText(terminalId, "terminalId cannot be blank");
        List<UserSessionEntity> userSessionEntityList = userSessionDAO.findByTerminalTypeAndTerminalId(terminalType, terminalId);
        int count = 0;
        if (!CollectionUtils.isEmpty(userSessionEntityList)) {
            for (UserSessionEntity userSessionEntity : userSessionEntityList) {
                UUID userId = userSessionEntity.getUserId();
                ReentrantLock userLock = UserLicenseHelper.getUserLockAlways(userId);
                userLock.lock();
                LOGGER.debug("用户[{}]在此加锁成功", userId);
                try {
                    userLicenseServiceTx.clearUserSession(userSessionEntity, clearType);
                    count++;
                } finally {
                    userLock.unlock();
                    LOGGER.debug("用户[{}]在此解锁成功", userId);
                }
            }
        }
        return count;

    }

    @Override
    public int clearTimeoutReportUserSession(UUID sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null");
        Optional<UserSessionEntity> optionalUserSessionEntity = userSessionDAO.findById(sessionId);
        if (optionalUserSessionEntity.isPresent()) {
            UserSessionEntity userSessionEntity = optionalUserSessionEntity.get();
            UUID userId = userSessionEntity.getUserId();
            ReentrantLock userLock = UserLicenseHelper.getUserLockAlways(userId);
            userLock.lock();
            LOGGER.debug("用户[{}]在此加锁成功", userId);
            try {
                userLicenseServiceTx.clearUserSession(userSessionEntity, ClearSessionReasonTypeEnum.TIMEOUT_REPORT);
                return 1;
            } finally {
                userLock.unlock();
                LOGGER.debug("用户[{}]在此解锁成功", userId);
            }
        }
        return 0;
    }

    @Override
    public List<UUID> findSessionUserIdListByTerminalId(TerminalTypeEnum terminalType, String terminalId) {
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.hasText(terminalId, "terminalId can not be empty");
        List<UserSessionEntity> userSessionEntityList = userSessionDAO.findByTerminalTypeAndTerminalId(terminalType, terminalId);
        if (!CollectionUtils.isEmpty(userSessionEntityList)) {
            return userSessionEntityList.stream().map(UserSessionEntity::getUserId).distinct().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void updateWebClientUserSessionInfo(UUID oldClusterId, UUID newClusterId) {
        Assert.notNull(oldClusterId, "oldClusterId can not be empty");
        Assert.notNull(newClusterId, "newClusterId can not be empty");

        userSessionDAO.updateWebClientUserSessionInfo(String.valueOf(oldClusterId), TerminalTypeEnum.WEB_CLIENT, String.valueOf(newClusterId));
    }
}
