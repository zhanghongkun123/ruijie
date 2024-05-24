package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 消息DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/6 16:24
 *
 * @author ketb
 */
public class AssistantRemoteResponseDTO {
    @Nullable
    private UUID deskId;

    @NotNull
    private Integer code;

    @Nullable
    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(@Nullable UUID deskId) {
        this.deskId = deskId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
