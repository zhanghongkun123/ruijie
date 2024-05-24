package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.UUID;

/**
 * Description: 镜像发布任务 DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.10.11
 *
 * @author liangyifeng
 */
public class GetSnapshotMaxNumDTO {

    private UUID imageTemplateId;

    private Integer maxNum;

    private Integer globalMaxNum;

    /**
     * 是否自动生成快照
     */
    private Boolean enableCreateSnapshot;

    /**
     * 是否开启多版本
     */
    private Boolean enableMultipleVersion;


    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Boolean getEnableCreateSnapshot() {
        return enableCreateSnapshot;
    }

    public void setEnableCreateSnapshot(Boolean enableCreateSnapshot) {
        this.enableCreateSnapshot = enableCreateSnapshot;
    }

    public Integer getGlobalMaxNum() {
        return globalMaxNum;
    }

    public void setGlobalMaxNum(Integer globalMaxNum) {
        this.globalMaxNum = globalMaxNum;
    }

    public Boolean getEnableMultipleVersion() {
        return enableMultipleVersion;
    }

    public void setEnableMultipleVersion(Boolean enableMultipleVersion) {
        this.enableMultipleVersion = enableMultipleVersion;
    }
}