package com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 更新桌面镜像模板
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-06-28
 *
 * @author zqj
 */
public class DesktopImageUpdateDTO {

    @NotNull
    private UUID desktopId;

    public DesktopImageUpdateDTO(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    @Override
    public String toString() {
        return "DesktopImageUpdateDTO{" +
                "desktopId=" + desktopId +
                '}';
    }
}

