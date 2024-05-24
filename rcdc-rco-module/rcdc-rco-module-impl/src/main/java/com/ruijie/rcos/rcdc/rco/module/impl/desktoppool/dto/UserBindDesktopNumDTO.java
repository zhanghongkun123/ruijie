package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/9
 *
 * @author linke
 */
public class UserBindDesktopNumDTO implements Serializable {

    private UUID desktopPoolId;

    private UUID userId;

    private Long bindNum;

    public UserBindDesktopNumDTO(UUID desktopPoolId, UUID userId, Long bindNum) {
        this.desktopPoolId = desktopPoolId;
        this.userId = userId;
        this.bindNum = bindNum;
    }

    public UserBindDesktopNumDTO(UUID userId, Long bindNum) {
        this.userId = userId;
        this.bindNum = bindNum;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Long getBindNum() {
        return bindNum;
    }

    public void setBindNum(Long bindNum) {
        this.bindNum = bindNum;
    }
}
