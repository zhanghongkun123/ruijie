package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.sm;

import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: 文件申请单上下文
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/25
 *
 * @author TD
 */
public interface AuditApplyContextResolver {

    String APPLY_ID = "applyId";
    
    String OLD_APPLY_STATE = "oldApplyState";

    String FAIL_REASON = "failReason";

    long DEFAULT_MAX_RETRY = TimeUnit.HOURS.toMillis(1);

    long DO_WAITING_RETRY = TimeUnit.SECONDS.toMillis(5);

    /**
     * 解析上下文中的申请单ID
     * @param context 上下文
     * @return 申请单ID
     */
    static UUID resolveApplyId(StateTaskHandle.StateProcessContext context) {
        Assert.notNull(context, "context can not be null");

        UUID extStorageId = context.get(APPLY_ID, UUID.class);
        Assert.notNull(extStorageId, "状态机上下文中必须存在参数[" + APPLY_ID + "]");
        return extStorageId;
    }

    /**
     * 解析上下文中的申请单的废弃原因
     * @param context 上下文
     * @return 废弃原因
     */
    static String resolveFailReason(StateTaskHandle.StateProcessContext context) {
        Assert.notNull(context, "context can not be null");

        String failReason = context.get(FAIL_REASON, String.class);
        Assert.hasText(failReason, "状态机上下文中必须存在参数[" + FAIL_REASON + "]");
        return failReason;
    }
}
