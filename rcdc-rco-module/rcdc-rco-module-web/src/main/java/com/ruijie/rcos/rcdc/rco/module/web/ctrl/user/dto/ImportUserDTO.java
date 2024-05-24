package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

import org.apache.commons.lang3.StringUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserImportEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/21
 *
 * @author Jarman
 */
public class ImportUserDTO {

    private Integer rowNum;

    private IacUserImportEnum isEdit;

    private String groupNames;

    private String userName;

    private String realName;

    private String phoneNum;

    private String state;

    private String email;

    private String openHardwareCertification;

    private String maxHardwareNum;

    private String openOtpCertification;

    private String openCasCertification;

    private String openSmsCertification;

    private String openRadiusCertification;

    private Boolean openHardwareCertificationBoolean = Boolean.TRUE;

    private Boolean openOtpCertificationBoolean = Boolean.TRUE;

    private Boolean openCasCertificationBoolean = Boolean.TRUE;

    private Boolean openRadiusCertificationBoolean = Boolean.FALSE;

    private Boolean shouldUpdatePassword = Boolean.TRUE;

    private String password;

    private String accountExpireDate;

    private String invalidTime;

    private String description;

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

    public String getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(String openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    public Boolean getOpenRadiusCertificationBoolean() {
        return openRadiusCertificationBoolean;
    }

    public void setOpenRadiusCertificationBoolean(Boolean openRadiusCertificationBoolean) {
        this.openRadiusCertificationBoolean = openRadiusCertificationBoolean;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public IacUserImportEnum getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(IacUserImportEnum isEdit) {
        this.isEdit = isEdit;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(String openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    public String getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(String openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    public String getOpenCasCertification() {
        return openCasCertification;
    }

    public void setOpenCasCertification(String openCasCertification) {
        this.openCasCertification = openCasCertification;
    }

    public Boolean getOpenHardwareCertificationBoolean() {
        return openHardwareCertificationBoolean;
    }

    public void setOpenHardwareCertificationBoolean(Boolean openHardwareCertificationBoolean) {
        this.openHardwareCertificationBoolean = openHardwareCertificationBoolean;
    }

    public Boolean getOpenOtpCertificationBoolean() {
        return openOtpCertificationBoolean;
    }

    public void setOpenOtpCertificationBoolean(Boolean openOtpCertificationBoolean) {
        this.openOtpCertificationBoolean = openOtpCertificationBoolean;
    }

    public Boolean getOpenCasCertificationBoolean() {
        return openCasCertificationBoolean;
    }

    public void setOpenCasCertificationBoolean(Boolean openCasCertificationBoolean) {
        this.openCasCertificationBoolean = openCasCertificationBoolean;
    }

    public Boolean getShouldUpdatePassword() {
        return shouldUpdatePassword;
    }

    public void setShouldUpdatePassword(Boolean shouldUpdatePassword) {
        this.shouldUpdatePassword = shouldUpdatePassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(String accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(String openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    public String getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(String maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
    }

    public String getOpenAccountPasswordCertification() {
        return openAccountPasswordCertification;
    }

    public void setOpenAccountPasswordCertification(String openAccountPasswordCertification) {
        this.openAccountPasswordCertification = openAccountPasswordCertification;
    }

    public String getOpenWorkWeixinCertification() {
        return openWorkWeixinCertification;
    }

    public void setOpenWorkWeixinCertification(String openWorkWeixinCertification) {
        this.openWorkWeixinCertification = openWorkWeixinCertification;
    }

    public String getOpenFeishuCertification() {
        return openFeishuCertification;
    }

    public void setOpenFeishuCertification(String openFeishuCertification) {
        this.openFeishuCertification = openFeishuCertification;
    }

    public String getOpenDingdingCertification() {
        return openDingdingCertification;
    }

    public void setOpenDingdingCertification(String openDingdingCertification) {
        this.openDingdingCertification = openDingdingCertification;
    }

    public String getOpenOauth2Certification() {
        return openOauth2Certification;
    }

    public void setOpenOauth2Certification(String openOauth2Certification) {
        this.openOauth2Certification = openOauth2Certification;
    }

    public String getOpenRjclientCertification() {
        return openRjclientCertification;
    }

    public void setOpenRjclientCertification(String openRjclientCertification) {
        this.openRjclientCertification = openRjclientCertification;
    }

    /**
     * 获取短信认证是否开启转BOOLEAN
     *
     * @return true:开启，false：未开启
     */
    public Boolean getOpenSmsCertificationToBoolean() {
        if (StringUtils.isEmpty(openSmsCertification)) {
            return Boolean.FALSE;
        }
        if (StringUtils.equals(openSmsCertification.trim(), LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_CERTIFICATION_ENABLE))) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
