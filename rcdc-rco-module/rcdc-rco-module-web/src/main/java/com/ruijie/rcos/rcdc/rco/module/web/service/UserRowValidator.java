package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserGroupValidateConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.constants.HardwareConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserDTO;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Description: 导入的用户数据行校验类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月20日
 *
 * @author Jarman
 */
public class UserRowValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRowValidator.class);

    private static final int PHONE_NUM_MAX_LENGTH = 32;

    private static final int PASSWORD_MAX_LENGTH = 32;

    private static final int PASSWORD_MIN_LENGTH = 6;

    private static final String PWD_PATTERN = "^[a-zA-Z0-9`~!@#$%^&*()_\\-+=<>?:\"{}|,./\\\\;'\\[\\]\\s]*$";

    /**
     * 分组层级分隔符
     */
    private static final String GROUP_SPILT = "/";

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String INVALID_PATTERN = "^[0-9]*$";

    private static final int INVALID_TIME_MAX_VALUE = 1000;

    private static final int INVALID_TIME_MIN_VALUE = 0;

    private static final int DESCRIPTION_MAX_LENGTH = 128;

    private static final String EXPIER_DATE_FLAG = "永不过期";

    private static final String INVALID_FLAG = "永不失效";

    private static final String GENERAL_FLAG = "0";

    /**
     * 数据行号
     */
    private Integer rowNum;

    private String userGroupName;

    /**
     * 用户组名，有多级
     */
    private List<String> userGroupNameList = Lists.newArrayList();

    /**
     * 记录校验数据不符合规范的国际化描述
     */
    List<String> errorList;

    private String userName;

    /**
     * 姓名
     */
    private String realName;

    private String email;

    private String phoneNum;

    /**
     * 用户状态
     */
    private String state;

    /**
     * 硬件特征码
     */
    private String openHardwareCertification;

    private String maxHardwareNum;

    /**
     * 动态口令
     */
    private String openOtpCertification;

    /**
     * Radius认证
     */
    private String openRadiusCertification;

    /**
     * 外部CAS认证
     */
    private String openCasCertification;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 过期时间
     */
    private String accountExpireDate;

    /**
     * 失效天数
     */
    private String invalidTime;

    /**
     * 描述
     */
    private String description;

    /**
     * 短信认证
     */
    private String openSmsCertification;

    /**
     * 本地密码认证
     */
    private String openAccountPasswordCertification;

    /**
     * 开启企业微信
     */
    private String openWorkWeixinCertification;

    /**
     * 开启飞书
     */
    private String openFeishuCertification;

    /**
     * 开启钉钉
     */
    private String openDingdingCertification;

    /**
     * 开启Oauth2
     */
    private String openOauth2Certification;

    /**
     * 开启锐捷客户端扫码
     */
    private String openRjclientCertification;

    protected UserRowValidator(ImportUserDTO user, List<String> errorList) {
        Assert.notNull(user, "行不能为null");
        Assert.notNull(errorList, "行不能为null");
        this.errorList = errorList;
        // row中的行数初始行为0，这里补偿+1
        this.rowNum = user.getRowNum() + 1;
        this.userGroupName = user.getGroupNames();
        this.setUserGroupNameList(userGroupName);
        this.userName = user.getUserName();
        this.realName = user.getRealName();
        this.email = user.getEmail();
        this.phoneNum = user.getPhoneNum();
        this.state = user.getState();
        this.openHardwareCertification = user.getOpenHardwareCertification();
        this.openOtpCertification = user.getOpenOtpCertification();
        this.openRadiusCertification = user.getOpenRadiusCertification();
        this.openCasCertification = user.getOpenCasCertification();
        this.password = user.getPassword();
        this.accountExpireDate = user.getAccountExpireDate();
        this.invalidTime = user.getInvalidTime();
        this.description = user.getDescription();
        this.openSmsCertification = user.getOpenSmsCertification();
        this.maxHardwareNum = user.getMaxHardwareNum();
        this.openAccountPasswordCertification = user.getOpenAccountPasswordCertification();
        this.openRjclientCertification = user.getOpenRjclientCertification();
        this.openWorkWeixinCertification = user.getOpenWorkWeixinCertification();
        this.openFeishuCertification = user.getOpenFeishuCertification();
        this.openDingdingCertification = user.getOpenDingdingCertification();
        this.openOauth2Certification = user.getOpenOauth2Certification();
    }

    private void setUserGroupNameList(String userGroupName) {
        if (StringUtils.hasText(userGroupName)) {
            String[] groupNameArr = userGroupName.split(GROUP_SPILT);
            userGroupNameList.addAll(Arrays.asList(groupNameArr));
        }
    }

    /**
     * 行内容数据校验
     *
     * @throws BusinessException 业务异常
     * @param isVdiModel 是否VDI服务器
     */
    public void validateRowData(boolean isVdiModel) throws BusinessException {
        String rowNumStr = String.valueOf(rowNum);

        validateUserName(rowNumStr);

        validateUserGroup(rowNumStr);

        validateRealName(rowNumStr);

        validateEmail(rowNumStr);

        validatePhone(rowNumStr);

        validateState(rowNumStr);

        if (isVdiModel) {
            validateOpenHardwareCertification(rowNumStr);
            validateMaxHardwareNum(rowNumStr);
            validateOpenRadiusCertification(rowNumStr);
            validateOpenSmsCertification(rowNumStr);
            validateOpenAccountPasswordCertification(rowNumStr);
            validateOpenWorkWeixinCertification(rowNumStr);
            validateOpenFeishuCertification(rowNumStr);
            validateOpenDingdingCertification(rowNumStr);
            validateOpenOauth2Certification(rowNumStr);
            checkCloseAll(rowNumStr);
        }

        validateOpenOtpCertification(rowNumStr);

        validatePassword(rowNumStr);

        validateCreateExpireDateFormat(rowNumStr);

        validateInvalidTime(rowNumStr);

        validateDescription(rowNumStr);

    }

    /**
     * 导入更新行内容数据校验
     *
     * @throws BusinessException 业务异常
     */
    public void validateUpdateRowData() throws BusinessException {
        String rowNumStr = String.valueOf(rowNum);

        validateUserName(rowNumStr);

        validateUserGroup(rowNumStr);

        validateRealName(rowNumStr);

        validateEmail(rowNumStr);

        validatePhone(rowNumStr);

        validateState(rowNumStr);

        validateOpenHardwareCertification(rowNumStr);

        validateMaxHardwareNum(rowNumStr);

        validateOpenOtpCertification(rowNumStr);

        validateOpenRadiusCertification(rowNumStr);

        validateOpenSmsCertification(rowNumStr);

        validatePassword(rowNumStr);

        validateExpireDateFormat(rowNumStr);

        validateInvalidTime(rowNumStr);

        validateDescription(rowNumStr);

        validateOpenAccountPasswordCertification(rowNumStr);
        validateOpenWorkWeixinCertification(rowNumStr);
        validateOpenFeishuCertification(rowNumStr);
        validateOpenDingdingCertification(rowNumStr);
        validateOpenOauth2Certification(rowNumStr);
        checkCloseAll(rowNumStr);

    }

    private void validateMaxHardwareNum(String rowNumStr) {
        boolean hasText = StringUtils.hasText(maxHardwareNum);
        // 如果有填写数量，校验数量是否填写正确
        if (hasText) {
            int maxHardwareNumInt = getMaxHardwareNumInt();
            if (maxHardwareNumInt < HardwareConstants.MIN_HARDWARE_NUM || maxHardwareNumInt > HardwareConstants.MAX_HARDWARE_NUM) {
                LOGGER.error("校验第[{}]行可绑定数量[{}]失败", rowNum, maxHardwareNum);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_MAX_HARDWARE_NUM_ILLEGAL, rowNumStr,
                        UserExcelField.MAX_HARDWARE_NUM.getHeader(), String.valueOf(HardwareConstants.MIN_HARDWARE_NUM),
                        String.valueOf(HardwareConstants.MAX_HARDWARE_NUM)));
                return;
            }
        }

        // 开启硬件特征码时，需要配置数量
        if (LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(StringUtils.trimWhitespace(openHardwareCertification))
                && !hasText) {
            LOGGER.error("开启硬件特征码时，需要配置可绑定数量。[{}]-[{}]", openHardwareCertification, maxHardwareNum);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_MAX_HARDWARE_NUM_MUST_NOT_NULL, rowNumStr,
                    UserExcelField.MAX_HARDWARE_NUM.getHeader()));
        }
    }

    /**
     * 将excel中的数据转换为int类型，如果填写的不是数字，则返回-1
     *
     * @return int类型数字
     */
    private int getMaxHardwareNumInt() {
        int parseInt;
        try {
            parseInt = Integer.parseInt(maxHardwareNum);
        } catch (NumberFormatException e) {
            return -1;
        }
        return parseInt;
    }

    private void validateOpenRadiusCertification(String rowNumStr) {
        if (!StringUtils.hasText(openRadiusCertification)) {
            LOGGER.error("第{}行Radius认证服务[{}]不能为空", rowNumStr, openRadiusCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_OPENRADIUSCERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        } else {
            if (LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openRadiusCertification.trim()) == false &&
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openRadiusCertification.trim()) == false) {
                LOGGER.error("第{}行Radius认证服务[{}]不合法", rowNumStr, openRadiusCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_OPENRADIUSERTIFICATION_ILLEGAL,
                        rowNumStr, openRadiusCertification));
            }
        }
    }

    private void validateOpenCasCertification(String rowNumStr) {
        if (!StringUtils.hasText(openCasCertification)) {
            LOGGER.error("第{}行外部CAS认证[{}]不能为空", rowNumStr, openCasCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_CASCERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        } else {
            if (LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openCasCertification.trim()) == false &&
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openCasCertification.trim()) == false) {
                LOGGER.error("第{}行外部CAS认证[{}]不合法", rowNumStr, openCasCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_CASCERTIFICATION_ILLEGAL,
                        rowNumStr, openCasCertification));
            }
        }
    }

    private void validateOpenOtpCertification(String rowNumStr) {
        if (!StringUtils.hasText(openOtpCertification)) {
            LOGGER.error("第{}行动态口令[{}]不能为空", rowNumStr, openOtpCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_OPTCERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        } else {
            if (LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openOtpCertification.trim()) == false &&
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openOtpCertification.trim()) == false) {
                LOGGER.error("第{}行动态口令[{}]不合法", rowNumStr, openOtpCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_OPTCERTIFICATION_ILLEGAL,
                        rowNumStr, openOtpCertification));
            }   
        }
    }

    private void validateOpenSmsCertification(String rowNumStr) {
        if (StringUtils.hasText(openSmsCertification)) {
            if (!LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openSmsCertification.trim()) &&
                    !LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openSmsCertification.trim())) {
                LOGGER.error("第{}行短信认证[{}]不合法", rowNumStr, openSmsCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_SMS_CERTIFICATION_ILLEGAL,
                        rowNumStr, openSmsCertification));
            }
        } else {
            LOGGER.error("第{}行短信认证[{}]不能为空", rowNumStr, openSmsCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_SMS_CERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        }
    }

    private void validateOpenAccountPasswordCertification(String rowNumStr) {
        if (StringUtils.hasText(openAccountPasswordCertification)) {
            if (!LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openAccountPasswordCertification.trim()) &&
                    !LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openAccountPasswordCertification.trim())) {
                LOGGER.error("第{}行本地密码认证[{}]不合法", rowNumStr, openAccountPasswordCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_ACCOUNT_PASSWORD_CERTIFICATION_ILLEGAL,
                        rowNumStr, openAccountPasswordCertification));
            }
        } else {
            LOGGER.error("第{}行本地密码认证[{}]不能为空", rowNumStr, openAccountPasswordCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_ACCOUNT_PASSWORD_CERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        }
    }

    private void validateOpenRjclientCertification(String rowNumStr) {
        if (StringUtils.hasText(openRjclientCertification)) {
            if (!LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openRjclientCertification.trim()) &&
                    !LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openRjclientCertification.trim())) {
                LOGGER.error("第{}行锐捷客户端扫码认证[{}]不合法", rowNumStr, openRjclientCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_RJCLIENT_CERTIFICATION_ILLEGAL,
                        rowNumStr, openRjclientCertification));
            }
        } else {
            LOGGER.error("第{}行锐捷客户端扫码认证[{}]不能为空", rowNumStr, openRjclientCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_RJCLIENT_CERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        }
    }

    private void validateOpenWorkWeixinCertification(String rowNumStr) {
        if (StringUtils.hasText(openWorkWeixinCertification)) {
            if (!LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openWorkWeixinCertification.trim()) &&
                    !LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openWorkWeixinCertification.trim())) {
                LOGGER.error("第{}行企业微信认证[{}]不合法", rowNumStr, openWorkWeixinCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_WORK_WEIXIN_CERTIFICATION_ILLEGAL,
                        rowNumStr, openWorkWeixinCertification));
            }
        } else {
            LOGGER.error("第{}行企业微信认证[{}]不能为空", rowNumStr, openWorkWeixinCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_WORK_WEIXIN_CERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        }
    }

    private void validateOpenFeishuCertification(String rowNumStr) {
        if (StringUtils.hasText(openFeishuCertification)) {
            if (!LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openFeishuCertification.trim()) &&
                    !LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openFeishuCertification.trim())) {
                LOGGER.error("第{}行飞书认证[{}]不合法", rowNumStr, openFeishuCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_FEISHU_CERTIFICATION_ILLEGAL,
                        rowNumStr, openFeishuCertification));
            }
        } else {
            LOGGER.error("第{}行飞书认证[{}]不能为空", rowNumStr, openFeishuCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_FEISHU_CERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        }
    }

    private void validateOpenDingdingCertification(String rowNumStr) {
        if (StringUtils.hasText(openDingdingCertification)) {
            if (!LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openDingdingCertification.trim()) &&
                    !LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openDingdingCertification.trim())) {
                LOGGER.error("第{}行钉钉认证[{}]不合法", rowNumStr, openDingdingCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_DINGDING_CERTIFICATION_ILLEGAL,
                        rowNumStr, openDingdingCertification));
            }
        } else {
            LOGGER.error("第{}行钉钉认证[{}]不能为空", rowNumStr, openDingdingCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_DINGDING_CERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        }
    }

    private void validateOpenOauth2Certification(String rowNumStr) {
        if (StringUtils.hasText(openOauth2Certification)) {
            if (!LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openOauth2Certification.trim()) &&
                    !LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openOauth2Certification.trim())) {
                LOGGER.error("第{}行AUTH2.0认证[{}]不合法", rowNumStr, openOauth2Certification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_OAUTH2_CERTIFICATION_ILLEGAL,
                        rowNumStr, openOauth2Certification));
            }
        } else {
            LOGGER.error("第{}行AUTH2.0认证[{}]不能为空", rowNumStr, openOauth2Certification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_OAUTH2_CERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        }
    }

    /**
     * @return true:所有都关闭 false:有开启认证
     */
    private void checkCloseAll(String rowNumStr) {
        if (LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(StringUtils.trimWhitespace(openAccountPasswordCertification)) &&
                LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(StringUtils.trimWhitespace(openWorkWeixinCertification)) &&
                LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(StringUtils.trimWhitespace(openFeishuCertification)) &&
                LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(StringUtils.trimWhitespace(openDingdingCertification)) &&
                LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(StringUtils.trimWhitespace(openOauth2Certification))) {
            LOGGER.error("第{}行主要认证至少选择一种", rowNumStr);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_MAIN_CERTIFICATION_ILLEGAL,
                    rowNumStr));
        }
    }

    private void validateOpenHardwareCertification(String rowNumStr) {
        if (!StringUtils.hasText(openHardwareCertification)) {
            LOGGER.error("第{}行硬件特征码[{}]不能为空", rowNumStr, openHardwareCertification);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_HARDWARECERTIFICATION_NOT_ALLOW_EMPTY,
                    rowNumStr));
        } else {
            if (LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_DISABLE).equals(openHardwareCertification.trim()) == false &&
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE).equals(openHardwareCertification.trim()) == false) {
                LOGGER.error("第{}行硬件特征码[{}]不合法", rowNumStr, openHardwareCertification);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_HARDWARECERTIFICATION_ILLEGAL,
                        rowNumStr, openHardwareCertification));
            }
        }
    }

    private void validateState(String rowNumStr) {
        if (!StringUtils.hasText(state)) {
            LOGGER.error("第{}行用户状态[{}]不能为空", rowNumStr, state);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERSTATE_NOT_ALLOW_EMPTY,
                    rowNumStr));
        } else {
            if (LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_STATE_DISABLE).equals(state.trim()) == false &&
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_STATE_ENABLE).equals(state.trim()) == false) {
                LOGGER.error("第{}行用户状态[{}]不合法", rowNumStr, state);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERSTATE_ILLEGAL,
                        rowNumStr, state));
            }
        }
    }

    private void validateEmail(String rowNumStr) {
        if (StringUtils.hasText(email) && !ValidatorUtil.isEmail(email)) {
            LOGGER.error("第{}行email[{}]不合法", rowNumStr, email);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_EMAIL_ILLEGAL, rowNumStr, email));
        }
    }

    private void validatePhone(String rowNumStr) {
        if (!StringUtils.hasText(phoneNum)) {
            LOGGER.error("第{}行手机号[{}]为空，不校验", rowNumStr, phoneNum);
            return;
        }
        if (phoneNum.trim().length() > PHONE_NUM_MAX_LENGTH ||
                !ValidatorUtil.isNumberInRangeForInteger(0L, TextShort.TEXT_SIZE, phoneNum.length())) {
            LOGGER.error("第{}行手机号[{}]不合法", rowNumStr, phoneNum);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_PHONE_ILLEGAL,
                    rowNumStr, phoneNum));
        }
    }

    private void validateRealName(String rowNumStr) {
        if (StringUtils.hasText(realName)) {
            if (realName.length() > UserValidateRules.REAL_NAME_SIZE) {
                LOGGER.error("第{}行姓名[{}]不能超过{}个字符", rowNumStr, realName, UserValidateRules.REAL_NAME_SIZE);
                // 姓名不能超过UserValidateRules.REAL_NAME_SIZE个字符
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_REALNAME_TOO_LENGTH,
                        rowNumStr, realName, String.valueOf(UserValidateRules.REAL_NAME_SIZE)));
            }
        } else {
            // 姓名不能为空
            LOGGER.error("第{}行姓名不能为空", rowNumStr);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_REALNAME_NOT_ALLOW_EMPTY,
                    rowNumStr));
        }
    }

    private void validateUserGroup(String rowNumStr) {
        if (CollectionUtils.isEmpty(userGroupNameList)) {
            LOGGER.debug("第[{}]行数据分组为空，不校验", rowNumStr);
            return;
        }
        // 分组中间不能有空字符串，即父分组不能为空字符串
        for (String name : userGroupNameList) {
            String nameNotSpace = name.trim();
            if (nameNotSpace.equals("")) {
                LOGGER.error("第{}行用户组父分组不能为空", rowNumStr);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERGROUP_NOT_EMPTY,
                        rowNumStr, userGroupName));
                break;
            }
        }
        if (userGroupNameList.size() > UserGroupValidateConstants.MAX_HIERARCHY_NUM) {
            // 用户组层级不能超过系统最多支持的9级
            LOGGER.error("第{}行用户组层级不能超过系统最多支持的{}级", rowNumStr, UserGroupValidateConstants.MAX_HIERARCHY_NUM);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_GROUP_OVER_COUNT,
                    rowNumStr, userGroupName, String.valueOf(UserGroupValidateConstants.MAX_HIERARCHY_NUM)));
        }
        for (String name : userGroupNameList) {
            String nameNotSpace = name.trim();
            if (nameNotSpace.equals("")) {
                continue;
            }
            if (!name.matches(UserValidateRules.USER_GROUP_NAME)) {
                // 用户组名格式错误
                LOGGER.error("第{}行用户组名[{}]格式错误", rowNumStr, userName);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_GROUPNAME_INCORRECT,
                        rowNumStr, name));
            }
            if (name.length() > UserValidateRules.USER_GROUP_NAME_SIZE) {
                // 用户组名不能超过UserValidateRules.USER_GROUP_NAME_SIZE个字符
                LOGGER.error("第{}行用户组名长度不能超过{}个字符", rowNumStr, UserValidateRules.USER_GROUP_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_GROUPNAME_TOO_LENGTH,
                        rowNumStr, name, String.valueOf(UserValidateRules.USER_GROUP_NAME_SIZE)));
            }
            if (UserValidateRules.DEFAULT_USER_GROUP_NAMES.contains(name)) {
                // 用户组名不能含有保留字
                LOGGER.error("第{}行用户组名[{}]不能包含保留字段{}", rowNumStr, UserValidateRules.DEFAULT_USER_NAMES.toString());
                errorList.add(
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_GROUPNAME_NOT_ALLOW_RESERVED,
                                rowNumStr, name));
            }
        }
    }

    private void validateUserName(String rowNumStr) {
        if (StringUtils.hasText(userName)) {
            if (!userName.matches(UserValidateRules.USER_NAME)) {
                LOGGER.error("第{}行用户名[{}]格式错误", rowNumStr, userName);
                // 用户名格式错误
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERNAME_INCORRECT,
                        rowNumStr, userName));

            }
            if (userName.length() > UserValidateRules.USER_NAME_SIZE) {
                // 用户名不能超过UserValidateRules.USER_NAME_SIZE个字符
                LOGGER.error("第{}行用户名长度不能超过{}个字符", rowNumStr, UserValidateRules.USER_NAME_SIZE);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERNAME_TOO_LENGTH,
                        rowNumStr, userName, String.valueOf(UserValidateRules.USER_NAME_SIZE)));
            }
            if (UserValidateRules.DEFAULT_USER_NAMES.contains(userName)) {
                // 用户名不能使用保留字段
                LOGGER.error("第{}行用户名[{}]不能包含保留字段{}", rowNumStr, UserValidateRules.DEFAULT_USER_NAMES.toString());
                errorList.add(
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERNAME_NOT_ALLOW_RESERVED,
                                rowNumStr, userName));
            }
        } else {
            // 用户名不能为空
            LOGGER.error("第{}行用户名[{}]不能为空", rowNumStr, userName);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_USERNAME_NOT_ALLOW_EMPTY,
                    rowNumStr, userName));
        }
    }

    private void validatePassword(String rowNumStr) {
        if (!StringUtils.hasText(password)) {
            LOGGER.error("第{}行密码[{}]为空，不校验", rowNumStr, password);
            return;
        }
        if (password.trim().equals(Constants.SECRET_USER_PASSWORD)) {
            LOGGER.error("第{}行密码[{}]为加密密码，不校验", rowNumStr, password);
            return;
        }
        if (password.trim().length() < PASSWORD_MIN_LENGTH || password.trim().length() > PASSWORD_MAX_LENGTH
                || !Pattern.matches(PWD_PATTERN, password)) {
            LOGGER.error("第{}行密码[{}]不合法", rowNumStr, password);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_IMPORT_PASSWORD_ILLEGAL,
                    new String[] {rowNumStr, password}));
        }

    }

    private void validateExpireDateFormat(String rowNumStr) throws BusinessException {
        if (StringUtils.isEmpty(accountExpireDate) || accountExpireDate.equals(EXPIER_DATE_FLAG) || accountExpireDate.equals(GENERAL_FLAG)) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date expireDate = null;
        try {
            expireDate = simpleDateFormat.parse(accountExpireDate);
        } catch (ParseException e) {
            LOGGER.error("第[{}]行过期时间[{}]格式解析错误,e:{}", rowNumStr, accountExpireDate, e);
            errorList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_IMPORT_EXPIRE_FORMAT_ERROR, rowNumStr, accountExpireDate));
        }
    }

    private void validateCreateExpireDateFormat(String rowNumStr) {
        if (StringUtils.isEmpty(accountExpireDate) || accountExpireDate.equals(EXPIER_DATE_FLAG) || accountExpireDate.equals(GENERAL_FLAG)) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date expireDate = simpleDateFormat.parse(accountExpireDate);
            if (expireDate.getTime() < new Date().getTime()) {
                LOGGER.error("第[{}]行过期时间[{}]小于当前时间[{}]", new String[]{rowNumStr, expireDate.toString(), new Date().toString()});
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_IMPORT_USER_VALIDATE_EXPIRE_DATE_FAIL,
                        rowNumStr, DateUtil.formatDate(expireDate, DATE_FORMAT), DateUtil.formatDate(new Date(), DATE_FORMAT)));
            }
        } catch (ParseException e) {
            LOGGER.error("第[{}]行过期时间[{}]格式解析错误, e:{}", rowNumStr, accountExpireDate, e);
            errorList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_IMPORT_EXPIRE_FORMAT_ERROR, rowNumStr, accountExpireDate));
        }
    }

    private void validateInvalidTime(String rowNumStr) throws BusinessException {
        if (StringUtils.isEmpty(invalidTime) || invalidTime.equals(INVALID_FLAG) || invalidTime.equals(GENERAL_FLAG)) {
            return;
        }
        try {
            int invalidDate = Integer.parseInt(invalidTime);
            if (invalidDate > INVALID_TIME_MAX_VALUE || invalidDate < INVALID_TIME_MIN_VALUE) {
                LOGGER.error("第[{}]行失效天数[{}]不在规定范围", rowNumStr, invalidTime);
                errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_INVALID_TIME_VALIDATE_FAIL, rowNumStr, invalidTime));
            }
        } catch (NumberFormatException e) {
            LOGGER.error("输入失效天数[{}]不合法,e:{}", invalidTime, e);
            errorList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_INVALID_TIME_VALIDATE_RESOLVE_FAIL, rowNumStr, invalidTime));
        }
    }

    private void validateDescription(String rowNumStr) {
        if (StringUtils.isEmpty(description)) {
            return;
        }
        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            LOGGER.error("第[{}]行描述信息长度超过限制", rowNumStr);
            errorList.add(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_USER_DESCRIPTION_VALIDATE_FAIL));
        }
    }
}
