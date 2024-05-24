package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;

import java.util.UUID;

/**
 * Description: 统计桌面池里面桌面对应的数量信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/20 9:31
 *
 * @author yxq
 */
public class StatisticDesktopPoolDesktopCountDTO {

    /**
     * 故障的桌面数量
     */
    private Integer errorCount;

    /**
     * 桌面数量
     */
    private Integer totalCount;

    /**
     * 已经使用的桌面数量
     */
    private Integer usedCount;

    /**
     * 桌面池id
     */
    private UUID desktopPoolId;

    /**
     * 桌面池类型
     */
    private DesktopPoolType desktopPoolType;

    /**
     * 桌面池类型(VDI、第三方)
     */
    private CbbDesktopPoolType cbbDesktopPoolType;

    public StatisticDesktopPoolDesktopCountDTO() {
        this.errorCount = 0;
        this.totalCount = 0;
        this.usedCount = 0;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public CbbDesktopPoolType getDesktopSourceType() {
        return cbbDesktopPoolType;
    }

    public void setDesktopSourceType(CbbDesktopPoolType cbbDesktopPoolType) {
        this.cbbDesktopPoolType = cbbDesktopPoolType;
    }
}
