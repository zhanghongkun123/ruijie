package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;

import java.util.Date;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/06
 *
 * @author tangxu
 */
public class ExportUserViewDTO implements Comparable<ExportUserViewDTO> {

    private String groupName;

    private String userName;

    private String realName;

    private String phoneNum;

    private String email;

    private IacUserStateEnum state;

    private IacUserTypeEnum userType;

    private Integer desktopNum;

    private Date createTime;

    private Boolean openAccountPasswordCertification;

    private Boolean openThirdPartyCertification;

    private Boolean openHardwareCertification;

    private Integer maxHardwareNum;

    private Boolean openOtpCertification;

    private Boolean openRadiusCertification;

    private Boolean openCasCertification;

    private Long accountExpireDate;

    private Boolean hasBindOtp;

    private String password;

    private Integer invalidTime;

    private String description;

    private String invalidDescription;

    private Boolean openSmsCertification;

    /**
     * 开启企业微信
     */
    private Boolean openWorkWeixinCertification;

    /**
     * 开启飞书
     */
    private Boolean openFeishuCertification;

    /**
     * 开启钉钉
     */
    private Boolean openDingdingCertification;

    /**
     * 开启Oauth2
     */
    private Boolean openOauth2Certification;

    /**
     * 开启锐捷客户端扫码
     */
    private Boolean openRjclientCertification;

    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public IacUserStateEnum getState() {
        return state;
    }

    public void setState(IacUserStateEnum state) {
        this.state = state;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public Integer getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(Integer desktopNum) {
        this.desktopNum = desktopNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getOpenAccountPasswordCertification() {
        return openAccountPasswordCertification;
    }

    public void setOpenAccountPasswordCertification(Boolean openAccountPasswordCertification) {
        this.openAccountPasswordCertification = openAccountPasswordCertification;
    }

    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    public Boolean getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(Boolean openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    public Boolean getOpenCasCertification() {
        return openCasCertification;
    }

    public void setOpenCasCertification(Boolean openCasCertification) {
        this.openCasCertification = openCasCertification;
    }

    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    @Override
    public int compareTo(ExportUserViewDTO o) {
        return this.groupName.compareTo(o.getGroupName());
    }

    public Boolean getHasBindOtp() {
        return hasBindOtp;
    }

    public void setHasBindOtp(Boolean hasBindOtp) {
        this.hasBindOtp = hasBindOtp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvalidDescription() {
        return invalidDescription;
    }

    public void setInvalidDescription(String invalidDescription) {
        this.invalidDescription = invalidDescription;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }

    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
    }

    public Boolean getOpenWorkWeixinCertification() {
        return openWorkWeixinCertification;
    }

    public void setOpenWorkWeixinCertification(Boolean openWorkWeixinCertification) {
        this.openWorkWeixinCertification = openWorkWeixinCertification;
    }

    public Boolean getOpenFeishuCertification() {
        return openFeishuCertification;
    }

    public void setOpenFeishuCertification(Boolean openFeishuCertification) {
        this.openFeishuCertification = openFeishuCertification;
    }

    public Boolean getOpenDingdingCertification() {
        return openDingdingCertification;
    }

    public void setOpenDingdingCertification(Boolean openDingdingCertification) {
        this.openDingdingCertification = openDingdingCertification;
    }

    public Boolean getOpenOauth2Certification() {
        return openOauth2Certification;
    }

    public void setOpenOauth2Certification(Boolean openOauth2Certification) {
        this.openOauth2Certification = openOauth2Certification;
    }

    public Boolean getOpenRjclientCertification() {
        return openRjclientCertification;
    }

    public void setOpenRjclientCertification(Boolean openRjclientCertification) {
        this.openRjclientCertification = openRjclientCertification;
    }
}
