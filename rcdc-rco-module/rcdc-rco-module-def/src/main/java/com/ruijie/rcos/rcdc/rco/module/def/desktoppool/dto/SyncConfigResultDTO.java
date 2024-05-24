package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import org.springframework.util.Assert;

/**
 * Description: 应用策略结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/28
 *
 * @author linke
 */
public class SyncConfigResultDTO {

    /**
     * 是否成功
     */
    private boolean isSuccess;

    /**
     * 信息
     */
    private String message;

    /**
     * 成功结果
     * @return SyncConfigResult
     */
    public static SyncConfigResultDTO success() {
        SyncConfigResultDTO result = new SyncConfigResultDTO();
        result.setIsSuccess(true);
        return result;
    }

    /**
     * 失败结果
     * @param message 消息
     * @return SyncConfigResult
     */
    public static SyncConfigResultDTO fail(String message) {
        Assert.hasText(message, "message must not null");
        SyncConfigResultDTO result = new SyncConfigResultDTO();
        result.setIsSuccess(false);
        result.setMessage(message);
        return result;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
