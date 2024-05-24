package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;

import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/01/21
 * @author liuchaoxue
 */
public class CbbGetCloudDesktopGroupDTO {

    @Nullable
    private UUID parentId;

    @NotBlank
    @TextShort
    private String groupName;

    public CbbGetCloudDesktopGroupDTO() {
    }

    public CbbGetCloudDesktopGroupDTO(UUID parentId, String groupName) {
        Assert.hasText(groupName, "groupName can not be blank");
        this.parentId = parentId;
        this.groupName = groupName;
    }

    public UUID getParentId() {
        return this.parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
