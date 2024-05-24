package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserValidatePwdRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ChangeUserPwdCode;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 修改用户密码帮助类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/28 15:44
 *
 * @author yxq
 */
@Service("rcoChangeUserPwdHelper")
public class ChangeUserPwdHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeUserPwdHelper.class);

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    /**
     * 构建修改用户密码同步修改管理员密码DTO
     *
     * @param userId      用户ID
     * @param newPassword 新密码，密文
     * @param oldPassword 旧密码，密文
     * @param apiCallerTypeEnum 密钥获取方式枚举
     * @return ModifyPasswordDTO
     */
    public ModifyPasswordDTO buildModifyPasswordDTO(UUID userId, String newPassword, String oldPassword, ApiCallerTypeEnum apiCallerTypeEnum) {
        Assert.notNull(userId, "userId must not null");
        Assert.notNull(newPassword, "newPassword must not null");
        Assert.notNull(oldPassword, "oldPassword must not null");
        Assert.notNull(apiCallerTypeEnum, "apiCallerTypeEnum must not null");
        LOGGER.info("构造的修改用户密码同步管理员密码信息");
        ModifyPasswordDTO modifyPasswordDTO = new ModifyPasswordDTO();
        modifyPasswordDTO.setId(userId);
        String decryptNewPwd = AesUtil.descrypt(newPassword, userInfoAPI.getUserPasswordKey(apiCallerTypeEnum));
        Boolean shouldUpdatePassword = certificationStrategyService.shouldUpdateAdminPwdFirstLogin(decryptNewPwd);
        modifyPasswordDTO.setNeedUpdatePassword(shouldUpdatePassword);
        modifyPasswordDTO.setNewPwd(decryptNewPwd);
        modifyPasswordDTO.setOldPwd(AesUtil.descrypt(oldPassword, userInfoAPI.getUserPasswordKey(apiCallerTypeEnum)));
        LOGGER.info("构造的修改用户密码同步管理员密码信息为：adminId=[{}],needUpdatePassword", modifyPasswordDTO.getId(),
                modifyPasswordDTO.getNeedUpdatePassword());

        return modifyPasswordDTO;
    }

    /**
     * 校验用户旧密码是否正确
     *
     * @param userName     用户名
     * @param pwdFromUser 用户输入的密码，密文
     * @param apiCallerTypeEnum 密钥获取方式枚举
     * @return 是否正确
     */
    public boolean checkOldPwd(String userName, String pwdFromUser, ApiCallerTypeEnum apiCallerTypeEnum) {
        Assert.hasText(userName, "userName cannot be empty");
        Assert.notNull(pwdFromUser, "pwdFromUser must not null");
        Assert.notNull(apiCallerTypeEnum, "apiCallerTypeEnum must not null");

        IacUserValidatePwdRequest iacUserValidatePwdRequest = new IacUserValidatePwdRequest();
        iacUserValidatePwdRequest.setUserName(userName);
        iacUserValidatePwdRequest.setPassword(pwdFromUser);
        String pwdFromUserKdy = userInfoAPI.getUserPasswordKey(apiCallerTypeEnum);
        iacUserValidatePwdRequest.setEncryptKey(pwdFromUserKdy);
        try {
            iacUserMgmtAPI.validateUserPassword(iacUserValidatePwdRequest);
            return true;
        } catch (BusinessException e) {
            LOGGER.error("校验用户密码失败：" + userName, e);
            return false;
        }
    }


    /**
     * 解码密码
     *
     * @param pwd  用户输入的密码，密文
     * @param apiCallerTypeEnum 密钥获取方式枚举
     * @return 解密的密码
     */
    public String descUserPwd(String pwd, ApiCallerTypeEnum apiCallerTypeEnum) {
        Assert.hasText(pwd, "pwd must not null");
        Assert.notNull(apiCallerTypeEnum, "apiCallerTypeEnum  must not null");
        String pwdFromUserKdy = userInfoAPI.getUserPasswordKey(apiCallerTypeEnum);
        return  AesUtil.descrypt(pwd, pwdFromUserKdy);
    }

    /**
     * 校验新密码是否符合当前设置的密码复杂度
     *
     * @param newPwd 新密码，密文
     * @param apiCallerTypeEnum 密钥获取方式枚举
     * @return 是否符合当前设置的密码复杂度
     */
    public boolean checkNwdPwd(String newPwd, ApiCallerTypeEnum apiCallerTypeEnum) {
        Assert.notNull(newPwd, "newPwd must not null");
        Assert.notNull(apiCallerTypeEnum, "apiCallerTypeEnum must not null");
        String key = userInfoAPI.getUserPasswordKey(apiCallerTypeEnum);
        String newPwdTxt = AesUtil.descrypt(newPwd, key);
        return certificationStrategyService.validatePwdByStrategyLevel(newPwdTxt);
    }

    /**
     * 修改错误次数，判断是否需要提示剩余错误次数还是锁定，需要锁定就锁定用户添加解锁时间信息
     *
     * @param userName     userName
     * @return 返回的错误码
     */
    public Integer checkNeedRemindOrLock(String userName) {
        Assert.notNull(userName, "userName must not null");

        IacLockInfoDTO lockInfo = certificationHelper.getUserAccountLockInfo(userName);
        // 未被锁定，触发更新锁信息
        if (IacLockStatus.UN_LOCKED == lockInfo.getLockStatus()) {
            return ChangeUserPwdCode.REMIND_ERROR_TIMES;
        }
        return ChangeUserPwdCode.IN_LOCKED;
    }

    /**
     * 校验新密码是否跟用户名一致
     * @param newPwd 新密码，密文
     * @param userName 用户名称
     * @return 是否符合当前设置的密码复杂度
     */
    public boolean pwdLikeName(String newPwd, String userName) {
        Assert.notNull(newPwd, "newPwd must not null");
        Assert.notNull(userName, "userName must not null");

        String newPwdTxt = AesUtil.descrypt(newPwd, RedLineUtil.getRealUserRedLine());

        return userName.equals(newPwdTxt);
    }
}