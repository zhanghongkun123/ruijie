package com.ruijie.rcos.rcdc.rco.module.def.api.wifi.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 *
 * Description: 删除wifi白名单实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class DeleteWifiWhitelistRequest {
    /**
     * 原终端组Id
     */
    private UUID oldTerminalGroupId;

    /**
     * 准备移动终端组ID
     */
    private UUID moveTerminalGroupId;

    /**
     * 需要通知的终端集合
     */
    private List<TerminalDTO> needNotifyTerminalList;

    public DeleteWifiWhitelistRequest()  { }

    public DeleteWifiWhitelistRequest(UUID oldTerminalGroupId, @Nullable UUID moveTerminalGroupId, List<TerminalDTO> needNotifyTerminalList) {
        this.oldTerminalGroupId = oldTerminalGroupId;
        this.moveTerminalGroupId = moveTerminalGroupId;
        this.needNotifyTerminalList = needNotifyTerminalList;
    }

    /**
     * 参数校验
     */
    public void verify() {
        Assert.notNull(oldTerminalGroupId, "oldTerminalGroupId can not be null");
    }

    public UUID getOldTerminalGroupId() {
        return oldTerminalGroupId;
    }

    public void setOldTerminalGroupId(UUID oldTerminalGroupId) {
        this.oldTerminalGroupId = oldTerminalGroupId;
    }

    public UUID getMoveTerminalGroupId() {
        return moveTerminalGroupId;
    }

    public void setMoveTerminalGroupId(UUID moveTerminalGroupId) {
        this.moveTerminalGroupId = moveTerminalGroupId;
    }

    public List<TerminalDTO> getNeedNotifyTerminalList() {
        return needNotifyTerminalList;
    }

    public void setNeedNotifyTerminalList(List<TerminalDTO> needNotifyTerminalList) {
        this.needNotifyTerminalList = needNotifyTerminalList;
    }
}
