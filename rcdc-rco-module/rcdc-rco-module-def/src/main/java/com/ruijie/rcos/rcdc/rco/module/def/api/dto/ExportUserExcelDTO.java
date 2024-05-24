package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Description: ????Excel??DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/01/11
 *
 * @author guoyongxin
 */
public class ExportUserExcelDTO implements Comparable<ExportUserExcelDTO> {

    private UUID groupId;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_GROUPNAME)
    private String groupName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_USERNAME)
    private String userName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_REALNAME)
    private String realName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_PHONENUM)
    private String phoneNum;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_EMAIL)
    private String email;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_STATE)
    private String state;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPENAHARDWARECERT)
    private String openHardwareCertification;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_MAX_HARDWARE_NUM)
    private Integer maxHardwareNum;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPENOTPCERT)
    private String openOtpCertification;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USER_PASSWORD)
    private String password;
    
    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPEN_SMS_CERT)
    private String openSmsCertification;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_ACCOUNTEXPIREDATE)
    private String accountExpireDate;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_INVALID_TIME)
    private String invalidTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_DESCRIPTION)
    private String description;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPEN_RADIUS_CERTIFICATION)
    private String openRadiusCertification;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPENACCOUNTPASSWORDCERT)
    private String openAccountPasswordCertification;

    /**
     * 开启企业微信
     */
    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPEN_WORK_WEIXIN_CERTIFICATION)
    private String openWorkWeixinCertification;

    /**
     * 开启飞书
     */
    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPEN_FEISHU_CERTIFICATION)
    private String openFeishuCertification;

    /**
     * 开启钉钉
     */
    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPEN_DINGDING_CERTIFICATION)
    private String openDingdingCertification;

    /**
     * 开启Oauth2
     */
    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPEN_OAUTH2_CERTIFICATION)
    private String openOauth2Certification;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_HASBINDOPT)
    private String hasBindOtp;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_OPENTHIRDPARTYCERT)
    private String openThirdPartyCertification;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_DESKTOPNUM)
    private String desktopNum;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_USERTYPE)
    private String userType;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_USER_COLUMNS_CREATETIME)
    private String createTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_INVALID_DESCRIPTION)
    private String invalidDescription;

    @Override
    public int compareTo(ExportUserExcelDTO o) {
        return this.groupName.compareTo(o.getGroupName());
    }

    public ExportUserExcelDTO() {
    }

    public ExportUserExcelDTO(ExportUserViewDTO exportUserViewDTO) {
        BeanUtils.copyProperties(exportUserViewDTO, this);
        this.userType = parseUserType(exportUserViewDTO.getUserType());
        this.state = parseState(exportUserViewDTO.getState());
        this.createTime = parseCreateTime(exportUserViewDTO.getCreateTime());
        this.openAccountPasswordCertification = parseCertification(exportUserViewDTO.getOpenAccountPasswordCertification());
        this.openThirdPartyCertification = parseCertification(exportUserViewDTO.getOpenThirdPartyCertification());
        this.openHardwareCertification = parseCertification(exportUserViewDTO.getOpenHardwareCertification());
        this.openOtpCertification = parseCertification(exportUserViewDTO.getOpenOtpCertification());
        this.openRadiusCertification = parseCertification(exportUserViewDTO.getOpenRadiusCertification());
        this.hasBindOtp = parseCertification(exportUserViewDTO.getHasBindOtp());
        this.accountExpireDate = parseAccountExpireDate(exportUserViewDTO.getAccountExpireDate());
        this.desktopNum = parseDesktopNum(exportUserViewDTO.getDesktopNum());
        this.invalidTime = parseInvalidTime(exportUserViewDTO.getInvalidTime());
        this.description = parseDescription(exportUserViewDTO.getDescription());
        this.invalidDescription = parseInvalidDescription(exportUserViewDTO.getInvalidDescription());
        this.openSmsCertification = parseCertification(exportUserViewDTO.getOpenSmsCertification());
        this.openWorkWeixinCertification = parseCertification(exportUserViewDTO.getOpenWorkWeixinCertification());
        this.openFeishuCertification = parseCertification(exportUserViewDTO.getOpenFeishuCertification());
        this.openDingdingCertification = parseCertification(exportUserViewDTO.getOpenDingdingCertification());
        this.openOauth2Certification = parseCertification(exportUserViewDTO.getOpenOauth2Certification());
    }

    private String parseInvalidDescription(String invalidDescription) {
        if (StringUtils.isEmpty(invalidDescription)) {
            return "";
        }
        return invalidDescription;
    }

    private String parseDescription(String description) {
        if (description == null) {
            return "";
        }
        return description;
    }

    private String parseInvalidTime(Integer invalidTime) {
        if (ObjectUtils.isEmpty(invalidTime) || invalidTime == 0) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_FOREVER_INVALID);
        }
        return invalidTime.toString();
    }

    private String parseDesktopNum(Integer desktopNum) {
        if (desktopNum == null) {
            return "";
        }
        return desktopNum.toString();
    }

    private String parseAccountExpireDate(Long accountExpireDate) {
        if (accountExpireDate == null) {
            return "";
        }
        if (accountExpireDate == 0) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_EXPORT_USER_ACCOUNT_NEVER_EXPIRE);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(new Date(accountExpireDate));
        }
    }

    private String parseCertification(Boolean bool) {
        if (bool == null) {
            return "";
        }
        if (bool) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_EXPORT_USER_CERTIFICATION_ENABLE);
        } else {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_EXPORT_USER_CERTIFICATION_DISABLE);
        }
    }

    private String parseCreateTime(Date date) {
        if (date == null) {
            return "";
        }
        return date.toString();
    }

    private String parseState(IacUserStateEnum cbbUserStateEnum) {
        if (cbbUserStateEnum == null) {
            return "";
        }
        String businessKey = null;
        switch (cbbUserStateEnum) {
            case DISABLE:
                businessKey = BusinessKey.RCDC_RCO_USER_STATE_DISABLE;
                break;
            case ENABLE:
                businessKey = BusinessKey.RCDC_RCO_USER_STATE_ENABLE;
                break;
            default:
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseUserType(IacUserTypeEnum userTypeEnum) {
        if (userTypeEnum == null) {
            return "";
        }
        String businessKey = null;
        switch (userTypeEnum) {
            case NORMAL:
                businessKey = BusinessKey.RCDC_RCO_USER_TYPE_NORMAL;
                break;
            case VISITOR:
                businessKey = BusinessKey.RCDC_RCO_USER_TYPE_VISITOR;
                break;
            case AD:
                businessKey = BusinessKey.RCDC_RCO_USER_TYPE_AD;
                break;
            case LDAP:
                businessKey = BusinessKey.RCDC_RCO_USER_TYPE_LDAP;
                break;
            default:
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    public String getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(String openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRealName() {
        return realName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getState() {
        return state;
    }

    public String getUserType() {
        return userType;
    }

    public String getDesktopNum() {
        return desktopNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getOpenAccountPasswordCertification() {
        return openAccountPasswordCertification;
    }

    public String getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public String getOpenOtpCertification() {
        return openOtpCertification;
    }

    public String getAccountExpireDate() {
        return accountExpireDate;
    }

    public String getHasBindOtp() {
        return hasBindOtp;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setDesktopNum(String desktopNum) {
        this.desktopNum = desktopNum;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setOpenAccountPasswordCertification(String openAccountPasswordCertification) {
        this.openAccountPasswordCertification = openAccountPasswordCertification;
    }

    public void setOpenHardwareCertification(String openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    public void setOpenOtpCertification(String openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    public void setAccountExpireDate(String accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public void setHasBindOtp(String hasBindOtp) {
        this.hasBindOtp = hasBindOtp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(String openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
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

    public String getInvalidDescription() {
        return invalidDescription;
    }

    public void setInvalidDescription(String invalidDescription) {
        this.invalidDescription = invalidDescription;
    }

    public String getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(String openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }

    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
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
}
