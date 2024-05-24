package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto.StatisticDesktopPoolDesktopCountDTO;

import javax.persistence.*;
import java.util.UUID;

/**
 * Description: 统计所有桌面池中不可用、在线、总的桌面数量 实体类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/8 10:16
 *
 * @author yxq
 */
@Entity
@Table(name = "v_rco_statistic_desktop_count")
public class ViewStatisticDesktopCountEntity {

    /**
     * 桌面池ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID desktopPoolId;

    /**
     * 桌面池类型
     */
    @Enumerated(EnumType.STRING)
    private DesktopPoolType desktopPoolType;

    /**
     * 桌面池类型(VDI、第三方)
     */
    @Enumerated(EnumType.STRING)
    private CbbDesktopPoolType desktopSourceType;

    /**
     * 总的桌面数量
     */
    private Integer totalCount;

    /**
     * 已连接的桌面数量
     */
    private Integer usedCount;

    /**
     * 不可用数量：故障的桌面数量
     */
    private Integer errorCount;

    @Version
    private Integer version;

    public ViewStatisticDesktopCountEntity() {
    }

    public ViewStatisticDesktopCountEntity(long totalCount, long usedCount, long errorCount) {
        this.totalCount = Math.toIntExact(totalCount);
        this.usedCount = Math.toIntExact(usedCount);
        this.errorCount = Math.toIntExact(errorCount);
    }

    /**
     * 转换成DTO
     *
     * @return DTO
     */
    public StatisticDesktopPoolDesktopCountDTO convertFor() {
        StatisticDesktopPoolDesktopCountDTO dto = new StatisticDesktopPoolDesktopCountDTO();
        dto.setDesktopPoolId(desktopPoolId);
        dto.setErrorCount(errorCount);
        dto.setTotalCount(totalCount);
        dto.setUsedCount(usedCount);
        dto.setDesktopPoolType(desktopPoolType);
        dto.setDesktopSourceType(desktopSourceType);
        return dto;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public CbbDesktopPoolType getDesktopSourceType() {
        return desktopSourceType;
    }

    public void setDesktopSourceType(CbbDesktopPoolType desktopSourceType) {
        this.desktopSourceType = desktopSourceType;
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

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
