package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.UUID;

import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年05月11日
 *
 * @author jarman
 */
public class UserGroupBatchTaskItem extends DefaultBatchTaskItem {

    private UUID groupId;

    public UserGroupBatchTaskItem(UUID itemId, String itemName) {
        super(itemId, itemName);
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }
}
