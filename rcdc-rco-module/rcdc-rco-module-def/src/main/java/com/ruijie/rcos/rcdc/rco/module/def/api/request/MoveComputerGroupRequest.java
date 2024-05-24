package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

import java.util.UUID;

/**
 * Description: 移动分组
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/31 17:15
 *
 * @author ketb
 */
public class MoveComputerGroupRequest implements Request {

    @NotNull
    private UUID computerId;

    @NotBlank
    private String computerName;

    @NotNull
    private UUID groupId;

    public UUID getComputerId() {
        return computerId;
    }

    public void setComputerId(UUID computerId) {
        this.computerId = computerId;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

}
