package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 同步云桌面个性化配置的变更
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/17
 *
 * @author zwf
 */
public class PushUserProfilePathUpdateBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushUserProfilePathUpdateBatchTaskHandler.class);

    private UserProfileStrategyNotifyAPI userProfileStrategyNotifyAPI;

    public PushUserProfilePathUpdateBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator,
                                                     UserProfileStrategyNotifyAPI userProfileStrategyNotifyAPI) {
        super(iterator);
        Assert.notNull(userProfileStrategyNotifyAPI, "the userProfileStrategyNotifyAPI is null.");
        this.userProfileStrategyNotifyAPI = userProfileStrategyNotifyAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "the batchTaskItem is null.");
        UUID deskId = batchTaskItem.getItemID();
        try {
            userProfileStrategyNotifyAPI.pushUserProfileUpdateToRunningDesk(deskId);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_NOTIFY_UPDATE_SUCCESS).msgArgs(deskId.toString()).build();
        } catch (BusinessException e) {
            LOGGER.error("向云桌面[{}]同步个性化配置变更失败,失败原因：", deskId, e);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_NOTIFY_UPDATE_FAIL, e, deskId.toString());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_NOTIFY_UPDATE_BATCH_SUCCESS);
    }
}
