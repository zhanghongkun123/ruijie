package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationStrategyRegexEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.CasScanCodeAuthBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.UserConfigHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.BatchUserOrGroupCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.CreateUserWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.UpdateUserBasicInfoWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserValidateRules;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: 创建用户参数校验
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/13
 *
 * @author Jarman
 */

@Service
public class UserCustomValidation {

    @Autowired
    private UserConfigHelper userConfigHelper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCustomValidation.class);

    private static final int PHONE_NUM_MAX_LENGTH = 32;

    /**
     * 校验创建用户请求参数
     *
     * @param request 请求参数对象
     * @throws BusinessException 业务异常
     */
    public void createUserValidate(CreateUserWebRequest request) throws BusinessException {
        Assert.notNull(request, "CreateUserWebRequest不能为null");
        String userName = request.getUserName();
        // 用户名不能是保留字
        if (UserValidateRules.DEFAULT_USER_NAMES.contains(userName)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_NAME_CONTAIN_DEFAULT, userName);
        }
        // 普通用户用户姓名不能为空
        if (IacUserTypeEnum.NORMAL == request.getUserType() && StringUtils.isBlank(request.getRealName())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_NORMAL_USER_REAL_NAME_MUST_NOT_BE_NULL);
        }

        // 校验用户密码
        validatePassword(request.getPassword(), request.getConfirmPwd());

        validateUserBaseInfo(request.getUserName(), request.getRealName(), request.getUserGroup(), request.getUserType());
        validateEmailAndPhone(request.getEmail(), request.getPhoneNum());
        VdiDesktopConfigVO config = request.getVdiDesktopConfig();
        if (config == null) {
            // 没有配置云桌面不校验
            LOGGER.debug("没有配置云桌面，不校验云桌面配置请求参数;userName:{}", request.getUserName());
            return;
        }
        if (IacUserTypeEnum.VISITOR == request.getUserType()) {
            // 访客用户校验，生成的桌面数必须大于0
            Integer visitorDesktopNum = config.getVisitorDesktopNum();
            if (visitorDesktopNum == null || visitorDesktopNum < 1) {
                LOGGER.debug("当前配置了{}个云桌面", visitorDesktopNum);
                throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_CONFIG_VDI_DESKTOP_NUM_NOT_RIGHT, String.valueOf(0));
            }
        }
        Assert.notNull(config.getImage(), "镜像模板配置不能为空");
        Assert.notNull(config.getImage().getId(), "镜像模板id不能为null");
        if (config.getNetwork() != null) {
            Assert.notNull(config.getNetwork().getAddress(), "网络地址池不能为null");
            Assert.notNull(config.getNetwork().getAddress().getId(), "网络地址池id不能为空");
            String ip = config.getNetwork().getAddress().getIp();
            if (StringUtils.isNotBlank(ip) && !ValidatorUtil.isIPv4Address(ip)) {
                LOGGER.info("ip[{}]不合法", ip);
                throw new BusinessException(UserBusinessKey.RCDC_RCO_INVALID_IP, ip);
            }
        }
        if (Optional.ofNullable(config.getPersonalDisk()).orElse(0) > 0 && Objects.isNull(config.getPersonDiskStoragePool())) {
            LOGGER.error("创建用户[{}]设置了本地盘，未设置对应的本地盘存储", request.getUserName());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_CONFIG_VDI_NO_PERSON_STORAGE_POOL);
        }
    }

    /**
     * 校验更新用户基本信息参数
     *
     * @param request 请求参数
     * @throws BusinessException business exception
     */
    public void updateUserValidate(UpdateUserBasicInfoWebRequest request) throws BusinessException {
        Assert.notNull(request, "UpdateUserBasicInfoWebRequest不能为null");

        validateUserBaseInfo(request.getUserName(), request.getRealName(), request.getUserGroup(), request.getUserType());
        validateEmailAndPhone(request.getEmail(), request.getPhoneNum());
    }

    private void validateEmailAndPhone(String email, String phone) throws BusinessException {
        if (StringUtils.isNotBlank(email) && !ValidatorUtil.isEmail(email)) {
            LOGGER.error("email[{}]不合法", email);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_INVALID_EMAIL, email);
        }
        if (StringUtils.isNotBlank(phone) && phone.trim().length() > PHONE_NUM_MAX_LENGTH) {
            LOGGER.error("手机号[{}]不合法", phone);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_INVALID_PHONE, phone);
        }
    }

    /**
     * 验证用户基本信息，包括：用户名是否为保留字、是否在未分组下创建用户、普通用户时姓名是否为空
     * 
     * @param userName 用户名
     * @param realName 用户姓名
     * @param userGroup 用户组
     * @param userTypeEnum 用户类型
     * @throws BusinessException 业务异常
     */
    private void validateUserBaseInfo(String userName, String realName, IdLabelEntry userGroup, IacUserTypeEnum userTypeEnum)
            throws BusinessException {

        // 用户名不能是保留字
        if (UserValidateRules.DEFAULT_USER_NAMES.contains(userName)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_NAME_CONTAIN_DEFAULT, userName);
        }

        // 未分组上不支持创建用户
        if (IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID.equals(userGroup.getId())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_GROUP_CAN_NOT_CREATE_IN_DEFAULT);
        }

        // 普通用户用户姓名不能为空
        if (IacUserTypeEnum.NORMAL == userTypeEnum && StringUtils.isBlank(realName)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_USER_NORMAL_USER_REAL_NAME_MUST_NOT_BE_NULL);
        }
    }

    /**
     * 批量应用用户或用户组认证策略
     * 
     * @param request 批量应用用户或用户组认证策略请求类
     * @throws BusinessException 业务异常
     */
    public void validateUserCertification(BatchUserOrGroupCertificationRequest request) throws BusinessException {
        Assert.notNull(request, "BatchUserOrGroupCertificationRequest不能为null");
        userConfigHelper.validateUserCertification(request.getPrimaryCertificationVO(), request.getAssistCertificationVO());
        // 批量更新时不支持用户和用户组ID都为空
        if (request.getUserIdArr().length == 0 && (request.getGroupIdArr().length == 0 && request.getUserSortGroupIdArr().length == 0)) {
            throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_USER_GROUP_BATCH_CERTIFICATION_PARAM_FAIL);
        }
    }

    /**
     * 修改用户密码校验
     *
     * @param request 修改用户密码请求
     * @throws BusinessException 业务异常
     */
    public void resetPasswordValidate(IdArrWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getPassword(), "request.getPassword() must not be null");
        Assert.notNull(request.getConfirmPwd(), "request.getConfirmPwd() must not be null");

        validatePassword(request.getPassword(), request.getConfirmPwd());
    }

    /**
     * 1.用户密码和确认密码是否一致
     * 2.密码长度格式等待
     * 
     * @param password 密码，密文
     * @param confirmPwd 确认密码，密文
     * @throws BusinessException 业务异常
     */
    private void validatePassword(String password, String confirmPwd) throws BusinessException {
        String decryptPassword;
        String decryptConfirmPwd;
        try {
            decryptPassword = AesUtil.descrypt(password, RedLineUtil.getRealUserRedLine());
            decryptConfirmPwd = AesUtil.descrypt(confirmPwd, RedLineUtil.getRealUserRedLine());
        } catch (Exception e) {
            LOGGER.error("解密失败，失败原因：", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_DECRYPT_FAIL, e);
        }
        if (StringUtils.isEmpty(decryptPassword)) {
            return;
        }

        if (!decryptPassword.equals(decryptConfirmPwd)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_PASSWORD_NOT_EQUAL_CONFIRM_PWD);
        }

        if (!Pattern.matches(CertificationStrategyRegexEnum.PATTERN_ONE.getPattern(), decryptPassword)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_PASSWORD_NOT_EFFECT);
        }
    }
}
