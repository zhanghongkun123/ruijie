package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;

/**
 * 
 * Description: DeleteUserGroupBatchTaskItem
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年5月19日
 * 
 * @author nt
 */
public class DeleteTerminalGroupBatchTaskItem extends DefaultBatchTaskItem {

    private UUID moveGroupId;

    public DeleteTerminalGroupBatchTaskItem(UUID itemId, String itemName, @Nullable UUID moveGroupId) {
        super(itemId, itemName);
        this.moveGroupId = moveGroupId;
    }

    public UUID getMoveGroupId() {
        return moveGroupId;
    }

    public void setMoveGroupId(UUID moveGroupId) {
        this.moveGroupId = moveGroupId;
    }
}
