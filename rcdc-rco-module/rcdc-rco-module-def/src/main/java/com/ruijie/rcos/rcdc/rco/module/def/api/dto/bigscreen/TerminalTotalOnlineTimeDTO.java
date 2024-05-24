package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

/**
 * Description:  终端在线总时长
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/7
 *
 * @author xiao'yong'deng
 */
public class TerminalTotalOnlineTimeDTO {

    /**
     * 终端类型
     */
    private String platform;

    /**
     * 在线总时间
     */
    private Long onlineTotalTime;

    public TerminalTotalOnlineTimeDTO(String platform, Long onlineTotalTime) {
        this.platform = platform;
        this.onlineTotalTime = onlineTotalTime;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Long getOnlineTotalTime() {
        return onlineTotalTime;
    }

    public void setOnlineTotalTime(Long onlineTotalTime) {
        this.onlineTotalTime = onlineTotalTime;
    }
}
