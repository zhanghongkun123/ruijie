package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 更新桌面池PC终端或终端组关联关系
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/31
 *
 * @author zqj
 */
public class UpdatePoolThirdPartyBindObjectDTO {

    @NotNull
    private UUID poolId;

    @Nullable
    private List<UUID> addComputerByIdList;

    @Nullable
    private List<UUID> selectedGroupIdList;

    @Nullable
    private List<UUID> removeGroupIdList;

    @Nullable
    private List<UUID> addGroupIdList;


    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    @Nullable
    public List<UUID> getAddComputerByIdList() {
        return addComputerByIdList;
    }

    public void setAddComputerByIdList(@Nullable List<UUID> addComputerByIdList) {
        this.addComputerByIdList = addComputerByIdList;
    }

    @Nullable
    public List<UUID> getSelectedGroupIdList() {
        return selectedGroupIdList;
    }

    public void setSelectedGroupIdList(@Nullable List<UUID> selectedGroupIdList) {
        this.selectedGroupIdList = selectedGroupIdList;
    }

    @Nullable
    public List<UUID> getRemoveGroupIdList() {
        return removeGroupIdList;
    }

    public void setRemoveGroupIdList(@Nullable List<UUID> removeGroupIdList) {
        this.removeGroupIdList = removeGroupIdList;
    }

    @Nullable
    public List<UUID> getAddGroupIdList() {
        return addGroupIdList;
    }

    public void setAddGroupIdList(@Nullable List<UUID> addGroupIdList) {
        this.addGroupIdList = addGroupIdList;
    }
}
