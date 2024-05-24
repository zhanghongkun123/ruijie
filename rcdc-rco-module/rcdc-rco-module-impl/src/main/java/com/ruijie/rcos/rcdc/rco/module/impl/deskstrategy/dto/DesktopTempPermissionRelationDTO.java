package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto;

import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/29
 *
 * @author linke
 */
public class DesktopTempPermissionRelationDTO {

    private UUID id;

    /**
     * 桌面临时权限ID
     */
    private UUID desktopTempPermissionId;

    /**
     * 关联对象ID
     */
    private UUID relatedId;

    /**
     * 对象类型
     */
    private DesktopTempPermissionRelatedType relatedType;

    /**
     * 是否发送过到期通知
     */
    private Boolean hasSendExpireNotice;

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

    public UUID getDesktopTempPermissionId() {
        return desktopTempPermissionId;
    }

    public void setDesktopTempPermissionId(UUID desktopTempPermissionId) {
        this.desktopTempPermissionId = desktopTempPermissionId;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public DesktopTempPermissionRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(DesktopTempPermissionRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public Boolean getHasSendExpireNotice() {
        return hasSendExpireNotice;
    }

    public void setHasSendExpireNotice(Boolean hasSendExpireNotice) {
        this.hasSendExpireNotice = hasSendExpireNotice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
