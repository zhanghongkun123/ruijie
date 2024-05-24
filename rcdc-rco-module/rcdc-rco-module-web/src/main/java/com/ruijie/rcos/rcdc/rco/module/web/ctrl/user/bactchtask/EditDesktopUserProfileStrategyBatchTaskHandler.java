package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;


import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * *编辑云桌面个性化配置异步批处理类
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年09月06日
 *
 * @author lihengjing
 */
public class EditDesktopUserProfileStrategyBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditDesktopUserProfileStrategyBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private String deskName;

    private UUID userProfileStrategyId;

    private String userProfileStrategyName;

    private CloudDesktopWebService cloudDesktopWebService;

    public EditDesktopUserProfileStrategyBatchTaskHandler(UserDesktopMgmtAPI cloudDesktopMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                          BaseAuditLogAPI auditLogAPI, CloudDesktopWebService cloudDesktopWebService) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    public EditDesktopUserProfileStrategyBatchTaskHandler(UserDesktopMgmtAPI cloudDesktopMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                          BaseAuditLogAPI auditLogAPI, String deskName,
                                                          CloudDesktopWebService cloudDesktopWebService) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        this.deskName = deskName;
        this.cloudDesktopWebService = cloudDesktopWebService;
    }

    public void setUserProfileStrategyId(@Nullable UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }


    public void setUserProfileStrategyName(@Nullable String userProfileStrategyName) {
        this.userProfileStrategyName = userProfileStrategyName;
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (deskName != null) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_SINGLE_RESULT_SUC)
                        .msgArgs(new String[] {deskName, userProfileStrategyName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_SINGLE_RESULT_FAIL)
                        .msgArgs(new String[] {deskName, userProfileStrategyName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_BATCH_RESULT);
        }
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        String logName = deskName;

        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = obtainDesktopById(taskItem.getItemID());
            if (Objects.nonNull(cloudDesktopDetailDTO)) {
                checkBeforeUpdate(cloudDesktopDetailDTO);
                logName = cloudDesktopDetailDTO.getDesktopName();
            }
            cloudDesktopMgmtAPI.updateDesktopUserProfileStrategy(taskItem.getItemID(), userProfileStrategyId);
            auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_SUC_LOG, logName, userProfileStrategyName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_ITEM_SUC_DESC)
                    .msgArgs(new String[] {logName, userProfileStrategyName}).build();
        } catch (BusinessException e) {
            LOGGER.error("变更云桌面个性化策略失败，云桌面id=" + taskItem.getItemID().toString(), e);
            auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_FAIL_LOG, logName, userProfileStrategyName,
                    e.getI18nMessage());
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_ITEM_FAIL_DESC, e, logName,
                    userProfileStrategyName, e.getI18nMessage());
        }
    }



    private CloudDesktopDetailDTO obtainDesktopById(UUID deskId) {
        try {
            return cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面数据失败，桌面id[{}]", deskId, e);
        }
        // 获取云桌面数据失败，返回null
        return null;
    }

    private void checkBeforeUpdate(CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        if (Objects.equals(cloudDesktopDetailDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_DYNAMIC_POOL_NOT_SUPPORT,
                    cloudDesktopDetailDTO.getDesktopName());
        }

        if (cloudDesktopDetailDTO.getSessionType() != CbbDesktopSessionType.MULTIPLE
                && CbbCloudDeskType.THIRD.name().equals(cloudDesktopDetailDTO.getDeskType())) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_DESKTOP_USER_PROFILE_STRATEGY_EDIT_SUC_THIRD_PARTY,
                    cloudDesktopDetailDTO.getDesktopName());
        }
    }

}
