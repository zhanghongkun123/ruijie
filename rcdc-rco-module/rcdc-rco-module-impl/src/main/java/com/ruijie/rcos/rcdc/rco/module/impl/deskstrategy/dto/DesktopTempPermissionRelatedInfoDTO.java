package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto;

import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.UUID;

/**
 * Description: DesktopTempPermissionRelatedDTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/27
 *
 * @author linke
 */
public class DesktopTempPermissionRelatedInfoDTO extends EqualsHashcodeSupport {

    private UUID relatedId;

    private DesktopTempPermissionRelatedType relatedType;

    private String relatedName;

    private Boolean hasSendExpireNotice;

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

    public String getRelatedName() {
        return relatedName;
    }

    public void setRelatedName(String relatedName) {
        this.relatedName = relatedName;
    }

    public Boolean getHasSendExpireNotice() {
        return hasSendExpireNotice;
    }

    public void setHasSendExpireNotice(Boolean hasSendExpireNotice) {
        this.hasSendExpireNotice = hasSendExpireNotice;
    }
}
