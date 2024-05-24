package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;

/**
 * Description: 用户组详细信息DTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 10:58 2020/5/17
 *
 * @author yxd
 */
public class UserGroupDetailDTO {

    private UUID id;

    private String name;

    private UUID parentId;

    private String description;

    /**
     *
     * VDI云桌面关联镜像模板id
     */
    private UUID cbbVdiDesktopImageId;

    /**
     * VDI云桌面关联策略模板id
     */
    private UUID cbbVdiDesktopStrategyId;

    /**
     * VDI云桌面关联网络模板id
     */
    private UUID cbbVdiDesktopNetworkId;

    /**
     * IDV云桌面关联镜像模板id
     */
    private UUID idvDesktopImageId;

    /**
     * IDV云桌面关联策略模板id
     */
    private UUID idvDesktopStrategyId;

    private Date createTime;

    private boolean isAdGroup;

    private boolean isLdapGroup;

    private IacAdUserAuthorityEnum adUserAuthority;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getCbbVdiDesktopImageId() {
        return cbbVdiDesktopImageId;
    }

    public void setCbbVdiDesktopImageId(UUID cbbVdiDesktopImageId) {
        this.cbbVdiDesktopImageId = cbbVdiDesktopImageId;
    }

    public UUID getCbbVdiDesktopStrategyId() {
        return cbbVdiDesktopStrategyId;
    }

    public void setCbbVdiDesktopStrategyId(UUID cbbVdiDesktopStrategyId) {
        this.cbbVdiDesktopStrategyId = cbbVdiDesktopStrategyId;
    }

    public UUID getCbbVdiDesktopNetworkId() {
        return cbbVdiDesktopNetworkId;
    }

    public void setCbbVdiDesktopNetworkId(UUID cbbVdiDesktopNetworkId) {
        this.cbbVdiDesktopNetworkId = cbbVdiDesktopNetworkId;
    }

    public UUID getIdvDesktopImageId() {
        return idvDesktopImageId;
    }

    public void setIdvDesktopImageId(UUID idvDesktopImageId) {
        this.idvDesktopImageId = idvDesktopImageId;
    }

    public UUID getIdvDesktopStrategyId() {
        return idvDesktopStrategyId;
    }

    public void setIdvDesktopStrategyId(UUID idvDesktopStrategyId) {
        this.idvDesktopStrategyId = idvDesktopStrategyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isAdGroup() {
        return isAdGroup;
    }

    public void setAdGroup(boolean adGroup) {
        isAdGroup = adGroup;
    }

    public boolean isLdapGroup() {
        return isLdapGroup;
    }

    public void setLdapGroup(boolean ldapGroup) {
        isLdapGroup = ldapGroup;
    }

    public IacAdUserAuthorityEnum getAdUserAuthority() {
        return adUserAuthority;
    }

    public void setAdUserAuthority(IacAdUserAuthorityEnum adUserAuthority) {
        this.adUserAuthority = adUserAuthority;
    }
}
