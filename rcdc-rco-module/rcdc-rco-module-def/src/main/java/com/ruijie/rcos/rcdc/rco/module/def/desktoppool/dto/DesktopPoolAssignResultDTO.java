package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import java.util.UUID;

/**
 * Description: 分配池桌面结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/12
 *
 * @author linke
 */
public class DesktopPoolAssignResultDTO {

    private UUID desktopId;

    private Integer code = 0;

    private String desktopPoolType;

    private String message;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(String desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
