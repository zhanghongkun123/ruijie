package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.GroupIdvDeskCfgVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.GroupVdiDeskCfgVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.GroupVoiDeskCfgVO;

import java.util.UUID;

/**
 * Description: 编辑分组回填数据
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/18
 *
 * @author Jarman
 */
public class UserGroupDetailWebResponse {

    private UUID id;

    private String groupName;

    private GroupIdLabelEntry parent;

    private GroupVdiDeskCfgVO vdiDesktopConfig;

    /** 登录权限等级 */
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    private GroupIdvDeskCfgVO idvDesktopConfig;

    /**
     * 用户组 VOI云桌面配置信息
     */
    private GroupVoiDeskCfgVO voiDesktopConfig;

    /** 开启硬件特征码 */
    private Boolean openHardwareCertification;

    /** 最大终端数 */
    private Integer maxHardwareNum;

    /** 开启动态口令 */
    private Boolean openOtpCertification;

    /**
     * CAS认证
     */
    private Boolean openCasCertification;

    /**
     * 账号密码认证
     */
    private Boolean openAccountPasswordCertification;

    /**
     * 第三方认证
     */
    private Boolean openThirdPartyCertification;

    /**
     * 随机密码
     */
    private Boolean passwordRandom;

    /**
     * 账号到期时间
     */
    private Long accountExpireDate;

    /**
     * 账号失效时间
     */
    private Integer invalidTime;

    /**
     * 开启短信认证
     */
    private Boolean openSmsCertification;

    /**
     * 是否开启Radius动态口令
     */
    private Boolean openRadiusCertification;

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

    public Boolean getPasswordRandom() {
        return passwordRandom;
    }

    public void setPasswordRandom(Boolean passwordRandom) {
        this.passwordRandom = passwordRandom;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupIdLabelEntry getParent() {
        return parent;
    }

    public void setParent(GroupIdLabelEntry parent) {
        this.parent = parent;
    }

    public GroupVdiDeskCfgVO getVdiDesktopConfig() {
        return vdiDesktopConfig;
    }

    public void setVdiDesktopConfig(GroupVdiDeskCfgVO vdiDesktopConfig) {
        this.vdiDesktopConfig = vdiDesktopConfig;
    }

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    public GroupIdvDeskCfgVO getIdvDesktopConfig() {
        return idvDesktopConfig;
    }

    public void setIdvDesktopConfig(GroupIdvDeskCfgVO idvDesktopConfig) {
        this.idvDesktopConfig = idvDesktopConfig;
    }

    public GroupVoiDeskCfgVO getVoiDesktopConfig() {
        return voiDesktopConfig;
    }

    public void setVoiDesktopConfig(GroupVoiDeskCfgVO voiDesktopConfig) {
        this.voiDesktopConfig = voiDesktopConfig;
    }

    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
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

    public Boolean getOpenAccountPasswordCertification() {
        return openAccountPasswordCertification;
    }

    public void setOpenAccountPasswordCertification(Boolean openAccountPasswordCertification) {
        this.openAccountPasswordCertification = openAccountPasswordCertification;
    }

    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
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
