package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserLockQueryRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacSecurityPolicy;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAccountStrategyInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23
 *
 * @author Jarman
 */
@Service("rcoLoginHelper")
public class LoginHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginHelper.class);

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserLoginSession userLoginSession;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityStrategyAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    /**
     * 绑定用户登录session
     *
     * @param userDetailDTO 用户信息
     * @param terminalId 终端id
     * @return 用户id
     */
    public UUID bindLoginSession(@Nullable IacUserDetailDTO userDetailDTO, String terminalId) {
        Assert.hasText(terminalId, "terminalId cannot be empty");
        // 认证成功，删除之前登录的session,绑定新的session
        userLoginSession.removeLoginUser(terminalId);
        if (userDetailDTO == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        UserInfo userInfo = new UserInfo(terminalId, userDetailDTO.getUserName(), userDetailDTO.getUserType());
        userInfo.setUserId(userDetailDTO.getId());
        userLoginSession.addLoginUser(userInfo);
        LOGGER.info("绑定用户session成功，用户信息为：{}", JSON.toJSONString(userInfo));
        return userDetailDTO.getId();
    }

    /**
     * 计算密码有效期剩余天数
     *
     * @param updatePwdTime 修改密码时间
     * @param limitDays 密码有效天数
     * @return 剩余密码有效天数
     */
    public int computePwdSurplusDays(Date updatePwdTime, int limitDays) {
        Assert.notNull(updatePwdTime, "updatePwdTime must not null");

        Date limitDate = DateUtil.computeDate(updatePwdTime, Calendar.DAY_OF_YEAR, limitDays);

        return DateUtil.computeDayInterval(limitDate.getTime() - System.currentTimeMillis());
    }

    /**
     * 用户安全红线信息封装
     *
     * @param userName 用户名
     * @param loginResultMessageDTO 用户安全红线信息
     */
    public void warpSafetyRedLine(String userName, LoginResultMessageDTO loginResultMessageDTO) {
        Assert.hasText(userName, "userName must not be empty");
        Assert.notNull(loginResultMessageDTO, "loginResultMessageDTO must not null");
        PwdStrategyDTO pwdStrategy = certificationStrategyParameterAPI.getPwdStrategy();
        IacLockInfoDTO lockInfoDTO = getUserAccountLockInfo(userName);
        loginResultMessageDTO.setErrorTimes(lockInfoDTO.getFailCount());
        loginResultMessageDTO.setLockTime(lockInfoDTO.getLockTime());
        loginResultMessageDTO.setPwdLockTime(pwdStrategy.getUserLockTime());
        loginResultMessageDTO.setPreventsBruteForce(pwdStrategy.getPreventsBruteForce());
        loginResultMessageDTO.setUserLockedErrorsTimes(pwdStrategy.getUserLockedErrorTimes());
    }

    /**
     * 是否启动安全防爆
     *
     * @return true(启动)/false(未启动)
     * @throws BusinessException BusinessException
     */
    public boolean isPreventsBruteForce() throws BusinessException {
        IacAccountStrategyInfoDTO accountStrategyInfo = iacSecurityStrategyAPI.getAccountStrategyInfo(SubSystem.CDC);
        // 是否开启安全红线
        return BooleanUtils.isTrue(accountStrategyInfo.getPreventsBruteForce());
    }

    private IacLockInfoDTO getUserAccountLockInfo(String userName) {
        Assert.hasText(userName, "userName cannot be empty");
        IacUserLockQueryRequest iacUserLockQueryRequest = new IacUserLockQueryRequest();
        iacUserLockQueryRequest.setUserName(userName);
        iacUserLockQueryRequest.setSecurityPolicy(IacSecurityPolicy.ACCOUNT);
        iacUserLockQueryRequest.setSubSystem(SubSystem.CDC);
        try {
            return iacUserMgmtAPI.getLockInfo(iacUserLockQueryRequest);
        } catch (Exception e) {
            throw new IllegalStateException("getUserAccountLockInfo error", e);
        }
    }
}
