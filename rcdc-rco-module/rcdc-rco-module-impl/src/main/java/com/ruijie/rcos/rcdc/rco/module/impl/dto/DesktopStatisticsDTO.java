package com.ruijie.rcos.rcdc.rco.module.impl.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/6
 *
 * @author Jarman
 */
public class DesktopStatisticsDTO {

    private Long count;

    private String deskState;

    public DesktopStatisticsDTO(Long count, String deskState) {
        this.count = count;
        this.deskState = deskState;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getDeskState() {
        return deskState;
    }

    public void setDeskState(String deskState) {
        this.deskState = deskState;
    }
}
