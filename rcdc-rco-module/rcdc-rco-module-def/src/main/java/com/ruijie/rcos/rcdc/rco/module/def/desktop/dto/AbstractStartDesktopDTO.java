package com.ruijie.rcos.rcdc.rco.module.def.desktop.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 启动云桌面抽象DTO
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/02
 *
 * @author linke
 */
public abstract class AbstractStartDesktopDTO implements Serializable {

    /** 云桌面id */
    @NotNull
    protected UUID desktopId;

    /** 镜像ID */
    @NotNull
    protected UUID imageTemplateId;

    /** 策略ID */
    @NotNull
    protected UUID strategyId;

    /** 定义RCC与虚机内部通信的acpi */
    @Nullable
    private String desktopAcpi;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    @Nullable
    public String getDesktopAcpi() {
        return desktopAcpi;
    }

    public void setDesktopAcpi(@Nullable String desktopAcpi) {
        this.desktopAcpi = desktopAcpi;
    }
}
