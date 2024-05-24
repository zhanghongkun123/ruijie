package com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/23
 *
 * @author linke
 */
public class ExtraDiskDTO implements Serializable {

    /*
     * 解决挂载先后顺序问题
     */
    @NotNull
    private Integer index;

    @Nullable
    private UUID diskId;

    @NotNull
    @Range(min = "1", max = "2048")
    private Integer extraSize;

    @Nullable
    private UUID assignedStoragePoolId;

    @NotNull
    private IdLabelEntry extraDiskStoragePool;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Nullable
    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(@Nullable UUID diskId) {
        this.diskId = diskId;
    }

    public Integer getExtraSize() {
        return extraSize;
    }

    public void setExtraSize(Integer extraSize) {
        this.extraSize = extraSize;
    }

    @Nullable
    public UUID getAssignedStoragePoolId() {
        return assignedStoragePoolId;
    }

    public void setAssignedStoragePoolId(@Nullable UUID assignedStoragePoolId) {
        this.assignedStoragePoolId = assignedStoragePoolId;
    }

    public IdLabelEntry getExtraDiskStoragePool() {
        return extraDiskStoragePool;
    }

    public void setExtraDiskStoragePool(IdLabelEntry extraDiskStoragePool) {
        this.extraDiskStoragePool = extraDiskStoragePool;
    }
}
