package com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * 用户硬件特征码关联信息DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public class RcoViewUserHardwareCertificationDTO extends EqualsHashcodeSupport {

    /**
     * 记录ID
     */
    private UUID id;

    /**
     * 用户ID
     */
    private UUID userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户组ID
     */
    private UUID userGroupId;


    /**
     * 用户组名称
     */
    private String userGroupName;

    /**
     * 终端ID
     */
    private String terminalId;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端IP
     */
    private String ip;

    /**
     * 状态
     */
    private IacUserHardwareCertificationStateEnum state;

    private String macAddr;

    /**
     * 特征码
     */
    private String featureCode;

    /**
     * 创建时间
     */
    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public IacUserHardwareCertificationStateEnum getState() {
        return state;
    }

    public void setState(IacUserHardwareCertificationStateEnum state) {
        this.state = state;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取大写格式的mac地址
     *
     * @return 大写格式的mac地址
     */
    public String getUpperCaseMac() {
        return StringUtils.isEmpty(macAddr) ? macAddr : macAddr.toUpperCase();
    }

    /**
     * 获取国际化特征码，如果特征码为空，则返回 --
     *
     * @return 获取国际化特征码
     */
    public String getI18nFeatureCode() {
        return StringUtils.isEmpty(featureCode) ?
                LocaleI18nResolver.resolve(BusinessKey.RCDC_HARDWARE_CERTIFICATION_EMPTY_FEATURE_CODE) : featureCode;
    }
}
