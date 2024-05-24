package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto;

import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * Description: 时间段内连接失败的数量或者使用率
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13 15:07
 *
 * @author yxq
 */
public class DesktopPoolStatisticsResultDTO {

    /**
     * 时间段
     */
    private String dateTime;

    /**
     * 连接失败的数量或者使用率
     */
    private Double count;

    public DesktopPoolStatisticsResultDTO(String dateTime, Double count) {
        this.dateTime = dateTime;
        this.count = count;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DesktopPoolStatisticsResultDTO that = (DesktopPoolStatisticsResultDTO) obj;
        return Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime);
    }
}
