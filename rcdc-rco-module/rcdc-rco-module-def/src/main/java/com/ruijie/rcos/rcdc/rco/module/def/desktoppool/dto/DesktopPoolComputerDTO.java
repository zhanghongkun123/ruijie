package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;

import java.util.Date;
import java.util.UUID;

/**
 *
 * Description: 桌面池与终端关系表示DTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public class DesktopPoolComputerDTO {

    private UUID id;

    private UUID desktopPoolId;

    private UUID relatedId;

    private String name;

    private ComputerRelatedType relatedType;

    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public ComputerRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(ComputerRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
