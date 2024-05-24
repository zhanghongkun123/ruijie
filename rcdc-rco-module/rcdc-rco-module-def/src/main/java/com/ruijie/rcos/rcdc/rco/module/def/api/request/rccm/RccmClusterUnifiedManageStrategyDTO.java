package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageForMasterClusterAllDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageSyncingImageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.RccmUnifiedManageRoleEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 同步模式配置请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/12
 *
 * @author WuShengQiang
 */
public class RccmClusterUnifiedManageStrategyDTO {

    /**
     * 主集群ID
     */
    @NotNull
    private UUID masterClusterId;

    /**
     * 是否启用:同步云桌面策略
     */
    @NotNull
    private Boolean enableSyncDesktopStrategy;

    /**
     * 是否启用:同步用户/用户组策略
     */
    @NotNull
    private Boolean enableSyncUser;

    /**
     * 角色
     */
    @NotNull
    private RccmUnifiedManageRoleEnum role;

    /**
     * 同步主集群的全量数据 true:需要处理 false:不处理
     */
    @Nullable
    private Boolean syncMasterClusterAllData;

    /**
     * 主集群的全量数据ID集合
     */
    @Nullable
    private List<UnifiedManageForMasterClusterAllDataDTO> masterClusterAllDataList;

    @Nullable
    private List<UnifiedManageSyncingImageDTO> syncingImageList;

    /**
     * 是否需要主集群推送全量数据 true:推 false:不推
     */
    @NotNull
    private Boolean needMasterClusterRefreshAllData;

    public UUID getMasterClusterId() {
        return masterClusterId;
    }

    public void setMasterClusterId(UUID masterClusterId) {
        this.masterClusterId = masterClusterId;
    }

    public Boolean getEnableSyncDesktopStrategy() {
        return enableSyncDesktopStrategy;
    }

    public void setEnableSyncDesktopStrategy(Boolean enableSyncDesktopStrategy) {
        this.enableSyncDesktopStrategy = enableSyncDesktopStrategy;
    }

    public Boolean getEnableSyncUser() {
        return enableSyncUser;
    }

    public void setEnableSyncUser(Boolean enableSyncUser) {
        this.enableSyncUser = enableSyncUser;
    }

    public RccmUnifiedManageRoleEnum getRole() {
        return role;
    }

    public void setRole(RccmUnifiedManageRoleEnum role) {
        this.role = role;
    }

    @Nullable
    public Boolean getSyncMasterClusterAllData() {
        return syncMasterClusterAllData;
    }

    public void setSyncMasterClusterAllData(@Nullable Boolean syncMasterClusterAllData) {
        this.syncMasterClusterAllData = syncMasterClusterAllData;
    }

    public List<UnifiedManageForMasterClusterAllDataDTO> getMasterClusterAllDataList() {
        return masterClusterAllDataList;
    }

    public void setMasterClusterAllDataList(List<UnifiedManageForMasterClusterAllDataDTO> masterClusterAllDataList) {
        this.masterClusterAllDataList = masterClusterAllDataList;
    }

    @Nullable
    public List<UnifiedManageSyncingImageDTO> getSyncingImageList() {
        return syncingImageList;
    }

    public void setSyncingImageList(@Nullable List<UnifiedManageSyncingImageDTO> syncingImageList) {
        this.syncingImageList = syncingImageList;
    }

    public Boolean getNeedMasterClusterRefreshAllData() {
        return needMasterClusterRefreshAllData;
    }

    public void setNeedMasterClusterRefreshAllData(Boolean needMasterClusterRefreshAllData) {
        this.needMasterClusterRefreshAllData = needMasterClusterRefreshAllData;
    }
}
