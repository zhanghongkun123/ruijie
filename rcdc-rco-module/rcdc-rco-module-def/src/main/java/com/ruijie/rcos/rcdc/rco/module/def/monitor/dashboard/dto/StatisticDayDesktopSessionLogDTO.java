package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto;


import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopStatisticsTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;

import java.util.UUID;

/**
 * Description: 统计过去一天桌面池连接失败数或者使用率DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/18 15:12
 *
 * @author yxq
 */
public class StatisticDayDesktopSessionLogDTO {

    /**
     * 连接失败数、桌面使用率
     */
    private Double count;

    /**
     * 关联的桌面池、桌面组id
     */
    private UUID relatedId;

    /**
     * 关联的类型：桌面池
     */
    private RelateTypeEnum relatedType;

    /**
     * 统计的类型
     */
    private DesktopStatisticsTypeEnum type;

    public StatisticDayDesktopSessionLogDTO() {
    }

    public StatisticDayDesktopSessionLogDTO(Double count, UUID relatedId, RelateTypeEnum relatedType, DesktopStatisticsTypeEnum type) {
        this.count = count;
        this.relatedId = relatedId;
        this.relatedType = relatedType;
        this.type = type;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public RelateTypeEnum getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(RelateTypeEnum relatedType) {
        this.relatedType = relatedType;
    }

    public DesktopStatisticsTypeEnum getType() {
        return type;
    }

    public void setType(DesktopStatisticsTypeEnum type) {
        this.type = type;
    }
}
