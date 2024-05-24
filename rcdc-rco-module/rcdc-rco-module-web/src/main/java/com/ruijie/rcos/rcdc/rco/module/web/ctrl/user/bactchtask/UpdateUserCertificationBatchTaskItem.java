package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/23
 *
 * @author TD
 */
public class UpdateUserCertificationBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private IacConfigRelatedType relatedType;

    private String successKey;

    private String failKey;

    private UUID[] filterUserIdArr;

    @Override
    public UUID getItemID() {
        return itemId;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public IacConfigRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(IacConfigRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getSuccessKey() {
        return successKey;
    }

    public void setSuccessKey(String successKey) {
        this.successKey = successKey;
    }

    public String getFailKey() {
        return failKey;
    }

    public void setFailKey(String failKey) {
        this.failKey = failKey;
    }

    public UUID[] getFilterUserIdArr() {
        return filterUserIdArr;
    }

    public void setFilterUserIdArr(UUID[] filterUserIdArr) {
        this.filterUserIdArr = filterUserIdArr;
    }
}
