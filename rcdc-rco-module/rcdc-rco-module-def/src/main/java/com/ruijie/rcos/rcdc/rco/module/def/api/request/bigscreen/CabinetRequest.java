package com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import java.util.UUID;
import org.springframework.lang.Nullable;

/**
 *
 * Description: 机柜请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public class CabinetRequest implements Request {

    @Nullable
    private UUID id;

    @NotBlank
    private String name;

    @Nullable
    private String description;

    @Nullable
    private Integer serverNum;

    @Nullable
    public Integer getServerNum() {
        return serverNum;
    }

    public void setServerNum(@Nullable Integer serverNum) {
        this.serverNum = serverNum;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}