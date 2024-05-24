package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeNodeVO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/17
 *
 * @author Jarman
 */
public class UserGroupVO extends TreeNodeVO {

    private boolean allowDelete;

    private boolean enableAd;

    private boolean enableLdap;

    private boolean enableDefault;

    private boolean enableThirdParty;

    private boolean disabled;

    private Integer totalUserNum;

    private Boolean isAssigned;

    private Integer assignedUserNum;

    private Integer disableUserNum;

    private Integer bindDiskNum;

    private Long accountExpireDate;

    private Integer invalidTime;

    private Integer bindDesktopNum;

    private Integer bindHostNum;

    public boolean isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public boolean isEnableAd() {
        return enableAd;
    }

    public void setEnableAd(boolean enableAd) {
        this.enableAd = enableAd;
    }

    public boolean isEnableDefault() {
        return enableDefault;
    }

    public void setEnableDefault(boolean enableDefault) {
        this.enableDefault = enableDefault;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean getEnableLdap() {
        return enableLdap;
    }

    public void setEnableLdap(boolean enableLdap) {
        this.enableLdap = enableLdap;
    }

    public Integer getTotalUserNum() {
        return totalUserNum;
    }

    public void setTotalUserNum(Integer totalUserNum) {
        this.totalUserNum = totalUserNum;
    }

    public Boolean getIsAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(Boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public Integer getAssignedUserNum() {
        return assignedUserNum;
    }

    public void setAssignedUserNum(Integer assignedUserNum) {
        this.assignedUserNum = assignedUserNum;
    }

    public Integer getDisableUserNum() {
        return disableUserNum;
    }

    public void setDisableUserNum(Integer disableUserNum) {
        this.disableUserNum = disableUserNum;
    }

    public Integer getBindDiskNum() {
        return bindDiskNum;
    }

    public void setBindDiskNum(Integer bindDiskNum) {
        this.bindDiskNum = bindDiskNum;
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

    public boolean getEnableThirdParty() {
        return enableThirdParty;
    }

    public void setEnableThirdParty(boolean enableThirdParty) {
        this.enableThirdParty = enableThirdParty;
    }

    public Integer getBindDesktopNum() {
        return bindDesktopNum;
    }

    public void setBindDesktopNum(Integer bindDesktopNum) {
        this.bindDesktopNum = bindDesktopNum;
    }

    public Integer getBindHostNum() {
        return bindHostNum;
    }

    public void setBindHostNum(Integer bindHostNum) {
        this.bindHostNum = bindHostNum;
    }
}
