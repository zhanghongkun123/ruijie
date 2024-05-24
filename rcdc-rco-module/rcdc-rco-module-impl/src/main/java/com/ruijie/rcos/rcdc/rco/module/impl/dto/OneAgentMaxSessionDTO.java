package com.ruijie.rcos.rcdc.rco.module.impl.dto;


import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 6修改的最大会话数
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/23
 *
 * @author zqj
 */
public class OneAgentMaxSessionDTO {

    /**
     * 修改的最大会话数
     */
    @NotNull
    private Integer maxSession;

    public Integer getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(Integer maxSession) {
        this.maxSession = maxSession;
    }
}
