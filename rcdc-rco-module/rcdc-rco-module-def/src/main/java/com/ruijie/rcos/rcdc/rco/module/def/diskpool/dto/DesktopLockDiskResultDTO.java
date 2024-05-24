package com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto;

/**
 * Description: 桌面锁定磁盘结果DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/25
 *
 * @author TD
 */
public class DesktopLockDiskResultDTO {

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
