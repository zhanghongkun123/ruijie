package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
public class ImportTerminalInfoDTO {

    /**
     * 终端组唯一标识
     */
    private Long oldTerminalGroupId;

    private UUID newTerminalGroupId;

    /**
     * 用户标识
     */
    private Long oldUserId;

    private UUID newUserId;

    /**
     * 策略标识
     */
    private UUID newStrategyId;

    /**
     * 镜像标识
     */
    private UUID newImageId;

    public UUID getNewStrategyId() {
        return newStrategyId;
    }

    public void setNewStrategyId(UUID newStrategyId) {
        this.newStrategyId = newStrategyId;
    }

    public UUID getNewImageId() {
        return newImageId;
    }

    public void setNewImageId(UUID newImageId) {
        this.newImageId = newImageId;
    }

    public Long getOldUserId() {
        return oldUserId;
    }

    public void setOldUserId(Long oldUserId) {
        this.oldUserId = oldUserId;
    }

    public UUID getNewUserId() {
        return newUserId;
    }

    public void setNewUserId(UUID newUserId) {
        this.newUserId = newUserId;
    }

    public Long getOldTerminalGroupId() {
        return oldTerminalGroupId;
    }

    public void setOldTerminalGroupId(Long oldTerminalGroupId) {
        this.oldTerminalGroupId = oldTerminalGroupId;
    }

    public UUID getNewTerminalGroupId() {
        return newTerminalGroupId;
    }

    public void setNewTerminalGroupId(UUID newTerminalGroupId) {
        this.newTerminalGroupId = newTerminalGroupId;
    }
}
