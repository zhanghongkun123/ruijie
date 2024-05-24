package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.constants.CertificationStrategyConstants;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.AuthenticationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.SyncUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 同步终端和服务器用户锁定信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/27
 *
 * @author yxq
 */
@DispatcherImplemetion(ShineAction.SYNC_USER_SECURITY_INFO)
public class SyncUserSecurityInfoSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncUserSecurityInfoSPIImpl.class);

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;
    
    @Autowired
    private UserService userService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");

        String terminalId = request.getTerminalId();
        String data = request.getData();
        LOGGER.info("终端[{}]上报用户锁定信息为：[{}]", terminalId, data);

        if (data == null) {
            LOGGER.error("终端[{}]上报的用户锁定信息失败，上报信息为空", terminalId);
            return;
        }

        SyncUserInfoDTO terminalUserInfo = JSON.parseObject(data, SyncUserInfoDTO.class);
        if (terminalUserInfo == null) {
            LOGGER.error("终端[{}]上报的用户锁定信息失败，解析信息为空", terminalId);
            return;
        }

        try {
            ViewUserDesktopEntity userDesktopEntity = viewDesktopDetailDAO.findByTerminalId(terminalId);
            if (userDesktopEntity == null) {
                LOGGER.error("终端[{}]上报的用户锁定信息失败，用户不存在", terminalId);
                responseFail(request, CommonMessageCode.CODE_ERR_OTHER);
                return;
            }
            UUID userId = userDesktopEntity.getUserId();
            String userName = userDesktopEntity.getUserName();

            RcoViewUserEntity viewUserEntity = userService.getUserInfoById(userId);
            PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
            boolean isLock;
            try {
                isLock = isUserLocked(viewUserEntity, terminalUserInfo, pwdStrategyDTO);
                LOGGER.info("用户[{}]锁定信息为：[{}]", userName, isLock);
            } catch (BusinessException e) {
                LOGGER.error(String.format("判断用户{%s}是否锁定失败", viewUserEntity.getUserName()), e);
                responseFail(request, CommonMessageCode.CODE_ERR_OTHER);
                return;
            }
            Date currentTime = new Date();
            Date realLockTime = getRealLockTime(isLock, terminalUserInfo, viewUserEntity, currentTime);
            Date realUnlockTime = getRealUnlockTime(isLock, viewUserEntity, pwdStrategyDTO, realLockTime);
            AuthenticationDTO authenticationDTO = new AuthenticationDTO();
            authenticationDTO.setResourceId(userId);
            authenticationDTO.setType(CertificationTypeEnum.USER);
            authenticationDTO.setLock(isLock);
            authenticationDTO.setLockTime(realLockTime);
            authenticationDTO.setUnlockTime(realUnlockTime);
            authenticationDTO.setPwdErrorTimes(viewUserEntity.getPwdErrorTimes() + terminalUserInfo.getErrorTimes());
            LOGGER.info("构建的用户[{}]锁定信息为：[{}]", userName, JSON.toJSONString(authenticationDTO));

            // 处理
            response(request, authenticationDTO, pwdStrategyDTO);
        } catch (Exception e) {
            LOGGER.error("同步用户锁定信息失败", e);
            responseFail(request, CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    private void responseFail(CbbDispatcherRequest request, Integer code) {
        try {
            shineMessageHandler.response(request, code);
        } catch (Exception e) {
            LOGGER.error("应答用户锁定信息失败");
        }
    }

    private void response(CbbDispatcherRequest request, AuthenticationDTO authenticationDTO, PwdStrategyDTO pwdStrategyDTO) {
        SyncUserInfoDTO syncUserInfoDTO = new SyncUserInfoDTO();
        syncUserInfoDTO.setErrorTimes(authenticationDTO.getPwdErrorTimes());
        syncUserInfoDTO.setLock(authenticationDTO.getLock());
        syncUserInfoDTO.setLockTime(authenticationDTO.getLockTime());
        syncUserInfoDTO.setPreventsBruteForce(pwdStrategyDTO.getPreventsBruteForce());
        syncUserInfoDTO.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
        syncUserInfoDTO.setUserLockedErrorsTimes(pwdStrategyDTO.getUserLockedErrorTimes());
        syncUserInfoDTO.setHasEditStrategy(false);
        syncUserInfoDTO.setTimestamp(System.currentTimeMillis());
        LOGGER.info("应答同步用户消息，syncUserInfoDTO = {}", JSON.toJSONString(syncUserInfoDTO));

        try {
            shineMessageHandler.responseSuccessContent(request, syncUserInfoDTO);
        } catch (Exception e) {
            LOGGER.error("应答同步用户消息失败，syncUserInfoDTO = {}", JSON.toJSONString(syncUserInfoDTO));
        }
    }

    /**
     * 获取用户真正的锁定时间
     *
     * @param isLock 用户是否锁定
     * @param terminalUserInfo 终端上报的用户信息
     * @param viewUserEntity 服务器中用户的信息
     * @param currentTime 当前时间
     * @return 真正的锁定时间，如果用户没有锁定，则返回null
     */
    private Date getRealLockTime(boolean isLock, SyncUserInfoDTO terminalUserInfo, RcoViewUserEntity viewUserEntity, Date currentTime)
            throws BusinessException {

        String userName = viewUserEntity.getUserName();
        if (!isLock) {
            LOGGER.info("用户[{}]未被锁定，无需计算锁定时间", userName);
            // 如果用户未被锁定，则锁定时间为null
            return null;
        }

        Date terminalLockTime = terminalUserInfo.getLockTime().getTime() == 0 ? null : terminalUserInfo.getLockTime();
        LOGGER.info("终端上报用户[{}]锁定时间为：[{}]", userName, terminalLockTime);

        Date rcdcLockTime = viewUserEntity.getLockTime();
        LOGGER.info("服务器用户[{}]锁定时间为：[{}]", userName, rcdcLockTime);

        if (terminalLockTime == null && rcdcLockTime == null) {
            LOGGER.info("用户[{}]在线和离线期间均未锁定，总和次数后被锁定，锁定时间为：[{}]", userName, currentTime);
            return currentTime;
        }

        if (terminalLockTime == null) {
            LOGGER.info("用户[{}]离线期间未被锁定，且在线期间被锁定，锁定时间为：[{}]", userName, rcdcLockTime);
            return rcdcLockTime;
        }

        if (rcdcLockTime == null) {
            LOGGER.info("用户[{}]在线期间未被锁定，且离线期间被锁定，锁定时间为：[{}]", userName, terminalLockTime);
            return terminalLockTime;
        }

        Date lockTime = terminalLockTime.before(rcdcLockTime) ? rcdcLockTime : terminalLockTime;
        LOGGER.info("用户[{}]在线和离线期间均被锁定，取较晚的锁定时间为锁定时间，锁定时间为：[{}]", userName, lockTime);

        return lockTime;
    }

    /**
     * 获取用户真正解锁的时间
     *
     * @param isLock 用户是否锁定
     * @param viewUserEntity 服务器中用户的信息
     * @param pwdStrategyDTO 安全策略
     * @param lockTime 锁定时间
     * @throws BusinessException 业务异常
     * @return 真正的解锁时间，如果用户没有锁定，则返回null
     */
    private Date getRealUnlockTime(boolean isLock, RcoViewUserEntity viewUserEntity, PwdStrategyDTO pwdStrategyDTO, Date lockTime)
            throws BusinessException {
        String userName = viewUserEntity.getUserName();
        if (!isLock) {
            LOGGER.info("用户[{}]未被锁定，无需计算解锁时间", userName);
            // 如果用户未被锁定，则解锁锁定时间为null
            return null;
        }

        Date unlockTime;
        if (pwdStrategyDTO.getUserLockTime() == CertificationStrategyConstants.PERMANENT_LOCK) {
            unlockTime = DateUtil.computeDate(lockTime, Calendar.YEAR, CertificationStrategyConstants.PERMANENT_LOCK_YEAR);
        } else {
            unlockTime = DateUtil.computeDate(lockTime, Calendar.MINUTE, pwdStrategyDTO.getUserLockTime());
        }

        LOGGER.info("用户[{}]锁定时间为：[{}]，最终解锁时间为：[{}]", userName, lockTime, unlockTime);
        return unlockTime;
    }

    /**
     * 若终端用户增加的错误次数和服务器中用户的错误次数之和超过配置的次数，
     * 或者其中一方已经被锁定，则用户被锁定
     *
     * @param viewUserEntity rcdc上用户信息
     * @param terminalUserInfo 终端同步上来的用户信息
     * @param pwdStrategyDTO 密码策略
     * @return 是否被锁定
     */
    private boolean isUserLocked(RcoViewUserEntity viewUserEntity, SyncUserInfoDTO terminalUserInfo, PwdStrategyDTO pwdStrategyDTO)
            throws BusinessException {
        String userName = viewUserEntity.getUserName();
        // RCDC上记录未锁定，但是终端连线后上报锁定
        if (terminalUserInfo.getLock() && BooleanUtils.isFalse(viewUserEntity.getLock())) {
            LOGGER.info("用户[{}]在离线期间被锁定，并且RCDC未被锁定", userName);
            auditLogAPI.recordLog(BusinessKey.RCDC_CERTIFICATION_USER_LOCKED, userName);
            return true;
        }
        Integer sumErrorTimes = viewUserEntity.getPwdErrorTimes() + terminalUserInfo.getErrorTimes();
        Integer userErrorTimes = pwdStrategyDTO.getUserLockedErrorTimes();
        if (sumErrorTimes >= userErrorTimes) {
            LOGGER.info("用户在线和离线期间错误次数之和为：[{}]，大于配置的允许错误次数[{}]", sumErrorTimes, userErrorTimes);
            // RCDC和终端记录均未锁定，但是错误次数之和超过
            if (!terminalUserInfo.getLock() && BooleanUtils.isFalse(viewUserEntity.getLock())) {
                LOGGER.info("用户之前未被锁定，并且在离线期间在服务器上的错误次数和终端上错误次数之和达到锁定次数，锁定用户");
                auditLogAPI.recordLog(BusinessKey.RCDC_CERTIFICATION_USER_LOCKED, userName);
            }
            return true;
        }

        return BooleanUtils.isTrue(viewUserEntity.getLock()) || terminalUserInfo.getLock();
    }
}
