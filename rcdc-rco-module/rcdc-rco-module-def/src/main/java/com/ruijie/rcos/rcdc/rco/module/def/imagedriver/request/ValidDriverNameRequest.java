package com.ruijie.rcos.rcdc.rco.module.def.imagedriver.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;

/**
 * <br>
 * Description: 驱动名称校验
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2023/07/11 <br>
 *
 * @author ypp
 */
public class ValidDriverNameRequest {

    @NotNull
    @TextName
    @Size(max = 64)
    private String driverName;

    @Nullable
    private UUID id;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }
}
