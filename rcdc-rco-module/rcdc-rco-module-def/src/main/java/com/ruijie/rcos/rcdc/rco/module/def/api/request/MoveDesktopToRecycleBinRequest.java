package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 创建云桌面数据请求参数
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author  artom
 */
public class MoveDesktopToRecycleBinRequest implements Request {

    @NotNull
    private UUID desktopId;

    @Nullable
    private UUID customTaskId;

    @Nullable
    public UUID getCustomTaskId() {
        return customTaskId;
    }

    public void setCustomTaskId(@Nullable UUID customTaskId) {
        this.customTaskId = customTaskId;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }
}
