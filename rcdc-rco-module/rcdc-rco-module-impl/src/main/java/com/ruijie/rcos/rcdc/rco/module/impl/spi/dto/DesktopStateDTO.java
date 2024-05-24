package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 
 * Description: 桌面状态类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/8
 *
 * @author zhiweiHong
 */
public class DesktopStateDTO {

    @NotNull
    private UUID id;


    /**
     * 云桌面状态
     */
    @JSONField(name = "deskState")
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
